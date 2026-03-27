package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.AssignmentResult;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RoomDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.StaffDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.*;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkAssignmentRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkRoomRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffCapacityRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final HkAssignmentRepository assignmentRepository;
    private final HkStaffCapacityRepository staffCapacityRepository;
    private final HkStaffRepository staffRepository;
    private final HkRoomRepository roomRepository;

    private static final int MAX_MINUTES = 240;

    @Transactional
    public AssignmentResult assign(
            LocalDate date,
            HkStaffShift shift,
            List<RoomDTO> rooms,
            List<StaffDTO> availableStaff
    ) {
        Map<Long, Integer> capacityMap = new HashMap<>();
        Map<Long, Integer> deepCleanCountMap = new HashMap<>();

        for (StaffDTO s : availableStaff) {
            capacityMap.put(s.getId(), s.getAllocatedMinutes() == null ? 0 : s.getAllocatedMinutes());
            deepCleanCountMap.put(s.getId(), 0);
        }

        List<RoomDTO> prioritized = new ArrayList<>();
        rooms.stream().filter(r -> r.getCleaningType() == HkRoomCleaningType.DEEP).forEach(prioritized::add);
        rooms.stream().filter(r -> r.getCleaningType() == HkRoomCleaningType.DAILY).forEach(prioritized::add);
        rooms.stream().filter(r -> r.getCleaningType() == HkRoomCleaningType.VACANT).forEach(prioritized::add);

        List<HkAssignment> assignmentsToSave = new ArrayList<>();
        List<RoomDTO> unassignedRooms = new ArrayList<>();

        for (RoomDTO room : prioritized) {
            int taskDuration = getDurationMinutes(room.getCleaningType());

            StaffDTO bestStaff = availableStaff.stream()
                    .filter(s -> {
                        int used = capacityMap.getOrDefault(s.getId(), 0);

                        if ((used + taskDuration) > MAX_MINUTES) {
                            return false;
                        }

                        if (room.getCleaningType() == HkRoomCleaningType.DEEP) {
                            return deepCleanCountMap.getOrDefault(s.getId(), 0) < 1;
                        }

                        return true;
                    })
                    .min(Comparator.comparingInt(s -> capacityMap.getOrDefault(s.getId(), 0)))
                    .orElse(null);

            if (bestStaff == null) {
                unassignedRooms.add(room);
                continue;
            }

            HkStaff staff = staffRepository.findById(bestStaff.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + bestStaff.getId()));

            HkRoom hkRoom = roomRepository.findById(room.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found: " + room.getId()));

            HkAssignment assignment = new HkAssignment();
            assignment.setStaff(staff);
            assignment.setRoom(hkRoom);
            assignment.setPropertyId(staff.getPropertyId());
            assignment.setDate(date);
            assignment.setShift(shift);
            assignment.setTaskType(mapTaskType(room.getCleaningType()));
            assignment.setDurationMinutes(taskDuration);
            assignment.setStatus(HkAssignmentStatus.PENDING);

            assignmentsToSave.add(assignment);

            capacityMap.merge(bestStaff.getId(), taskDuration, Integer::sum);

            if (room.getCleaningType() == HkRoomCleaningType.DEEP) {
                deepCleanCountMap.merge(bestStaff.getId(), 1, Integer::sum);
            }

            if (hkRoom.getAssignedShift() == null) {
                hkRoom.setAssignedShift(shift);
            }
        }

        assignmentRepository.saveAll(assignmentsToSave);

        List<HkStaffCapacity> capacitiesToSave = new ArrayList<>();
        for (StaffDTO staffDto : availableStaff) {
            HkStaff staff = staffRepository.findById(staffDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + staffDto.getId()));

            HkStaffCapacity capacity = staffCapacityRepository
                    .findByStaffAndDateAndShift(staff, date, shift)
                    .orElseGet(() -> HkStaffCapacity.builder()
                            .staff(staff)
                            .date(date)
                            .shift(shift)
                            .allocatedMinutes(0)
                            .maxMinutes(MAX_MINUTES)
                            .build());

            capacity.setAllocatedMinutes(capacityMap.getOrDefault(staff.getId(), 0));
            capacity.setMaxMinutes(MAX_MINUTES);
            capacitiesToSave.add(capacity);
        }

        staffCapacityRepository.saveAll(capacitiesToSave);

        return AssignmentResult.builder()
                .created(assignmentsToSave)
                .unassignedRooms(unassignedRooms)
                .hasShortfall(!unassignedRooms.isEmpty())
                .build();
    }

    private int getDurationMinutes(HkRoomCleaningType cleaningType) {
        return switch (cleaningType) {
            case DEEP -> 120;
            case DAILY -> 30;
            case VACANT -> 15;
        };
    }

    private HkAssignmentTaskType mapTaskType(HkRoomCleaningType cleaningType) {
        return switch (cleaningType) {
            case DEEP -> HkAssignmentTaskType.DEEP;
            case DAILY -> HkAssignmentTaskType.DAILY;
            case VACANT -> HkAssignmentTaskType.VACANT;
        };
    }
}