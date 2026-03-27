package com.kickdrum.HousekeepingWorkflowManagementSystem.service;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RoomDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ShortfallReport;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.StaffDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.*;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkRoomRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkShortfallAlertRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkShortfallRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShortfallService {

    private final HkShortfallAlertRepository shortfallAlertRepository;
    private final HkShortfallRoomRepository shortfallRoomRepository;
    private final HkRoomRepository roomRepository;

    public ShortfallReport calculate(
            LocalDate date,
            List<RoomDTO> rooms,
            List<StaffDTO> morningStaff,
            List<StaffDTO> afternoonStaff
    ) {
        int totalRequiredMinutes = rooms.stream()
                .mapToInt(room -> getDurationMinutes(room.getCleaningType()))
                .sum();

        int totalStaff = morningStaff.size() + afternoonStaff.size();
        int totalAvailableMinutes = totalStaff * 240;

        boolean hasShortfall = totalRequiredMinutes > totalAvailableMinutes;
        int extraStaffNeeded = 0;

        if (hasShortfall) {
            int gap = totalRequiredMinutes - totalAvailableMinutes;
            extraStaffNeeded = (int) Math.ceil(gap / 240.0);
        }

        return ShortfallReport.builder()
                .date(date)
                .totalRequiredMinutes(totalRequiredMinutes)
                .totalAvailableMinutes(totalAvailableMinutes)
                .hasShortfall(hasShortfall)
                .extraStaffNeeded(extraStaffNeeded)
                .build();
    }

    @Transactional
    public void persistShortfall(
            LocalDate date,
            HkStaffShift shift,
            List<RoomDTO> unassignedRooms
    ) {
        if (unassignedRooms == null || unassignedRooms.isEmpty()) {
            return;
        }

        int totalMinutes = unassignedRooms.stream()
                .mapToInt(room -> getDurationMinutes(room.getCleaningType()))
                .sum();

        int extraStaffNeeded = (int) Math.ceil(totalMinutes / 240.0);

        HkShortfallAlert alert = new HkShortfallAlert();
        alert.setDate(date);
        alert.setShift(shift);
        alert.setExtraStaffNeeded(extraStaffNeeded);
        alert.setResolved(false);

        HkShortfallAlert savedAlert = shortfallAlertRepository.save(alert);

        List<HkShortfallRoom> shortfallRooms = unassignedRooms.stream()
                .map(roomDto -> {
                    HkRoom room = roomRepository.findById(roomDto.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomDto.getId()));

                    HkShortfallRoom shortfallRoom = new HkShortfallRoom();
                    shortfallRoom.setShortfallAlert(savedAlert);
                    shortfallRoom.setRoom(room);
                    shortfallRoom.setTaskType(mapTaskType(roomDto.getCleaningType()));
                    return shortfallRoom;
                })
                .toList();

        shortfallRoomRepository.saveAll(shortfallRooms);
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