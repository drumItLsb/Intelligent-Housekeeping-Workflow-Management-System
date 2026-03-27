package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.*;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.*;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkRoomRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffCapacityRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final BookingMockService bookingMockService;
    private final HkRoomRepository roomRepository;
    private final HkStaffRepository staffRepository;
    private final HkStaffCapacityRepository staffCapacityRepository;
    private final AssignmentService assignmentService;
    private final ShortfallService shortfallService;

    @Transactional
    public SchedulerResult runAssignment(UUID propertyId, LocalDate date) {

        List<IbeRoomDTO> bookedRooms = bookingMockService.getBookedRooms(propertyId, date);

        List<HkRoom> classifiedRooms = new ArrayList<>();

        for (IbeRoomDTO ibeRoom : bookedRooms) {
            HkRoom hkRoom = roomRepository.findByIbeRoomId(ibeRoom.getRoomId())
                    .orElseGet(HkRoom::new);

            hkRoom.setIbeRoomId(ibeRoom.getRoomId());
            hkRoom.setRoomNumber(ibeRoom.getRoomNumber());
            hkRoom.setCheckinDate(ibeRoom.getCheckIn());
            hkRoom.setCheckoutDate(ibeRoom.getCheckOut());

            if (ibeRoom.getOccupancyStatus() == HkRoomOccupancyStatus.VACANT) {
                hkRoom.setOccupancyStatus(HkRoomOccupancyStatus.VACANT);
                hkRoom.setCleaningType(HkRoomCleaningType.VACANT);
            } else if (ibeRoom.getCheckOut() != null && ibeRoom.getCheckOut().equals(date)) {
                hkRoom.setOccupancyStatus(HkRoomOccupancyStatus.OCCUPIED);
                hkRoom.setCleaningType(HkRoomCleaningType.DEEP);
            } else {
                hkRoom.setOccupancyStatus(HkRoomOccupancyStatus.OCCUPIED);
                hkRoom.setCleaningType(HkRoomCleaningType.DAILY);
            }

            hkRoom.setAssignedShift(null);
            hkRoom.setIsCleaned(false);

            classifiedRooms.add(hkRoom);
        }

        roomRepository.saveAll(classifiedRooms);

        List<RoomDTO> roomDTOs = classifiedRooms.stream()
                .map(this::toRoomDTO)
                .toList();

        List<StaffDTO> morningStaff = getAvailableStaffForShift(propertyId, date, HkStaffShift.MORNING);
        List<StaffDTO> afternoonStaff = getAvailableStaffForShift(propertyId, date, HkStaffShift.AFTERNOON);

        ShortfallReport earlyWarning = shortfallService.calculate(
                date,
                roomDTOs,
                morningStaff,
                afternoonStaff
        );

        AssignmentResult morningResult = assignmentService.assign(
                date,
                HkStaffShift.MORNING,
                roomDTOs,
                morningStaff
        );

        AssignmentResult afternoonResult = assignmentService.assign(
                date,
                HkStaffShift.AFTERNOON,
                morningResult.getUnassignedRooms(),
                afternoonStaff
        );

        if (!afternoonResult.getUnassignedRooms().isEmpty()) {
            shortfallService.persistShortfall(
                    date,
                    HkStaffShift.AFTERNOON,
                    afternoonResult.getUnassignedRooms()
            );
        }

        return SchedulerResult.builder()
                .date(date)
                .morningAssigned(morningResult.getCreated().size())
                .afternoonAssigned(afternoonResult.getCreated().size())
                .unassignedRooms(afternoonResult.getUnassignedRooms())
                .earlyWarning(earlyWarning)
                .build();
    }

    private List<StaffDTO> getAvailableStaffForShift(
            UUID propertyId,
            LocalDate date,
            HkStaffShift shift
    ) {
        List<HkStaff> staffList = staffRepository.findByPropertyIdAndShiftAndAvailability(
                propertyId,
                shift,
                HkStaffAvailability.ON_DUTY
        );

        List<StaffDTO> result = new ArrayList<>();

        for (HkStaff staff : staffList) {
            HkStaffCapacity capacity = staffCapacityRepository
                    .findByStaffAndDateAndShift(staff, date, shift)
                    .orElseGet(() -> HkStaffCapacity.builder()
                            .staff(staff)
                            .date(date)
                            .shift(shift)
                            .allocatedMinutes(0)
                            .maxMinutes(240)
                            .build());

            result.add(StaffDTO.builder()
                    .id(staff.getId())
                    .name(staff.getName())
                    .propertyId(staff.getPropertyId())
                    .shift(staff.getShift())
                    .allocatedMinutes(capacity.getAllocatedMinutes())
                    .maxMinutes(capacity.getMaxMinutes())
                    .build());
        }

        return result;
    }

    private RoomDTO toRoomDTO(HkRoom room) {
        return RoomDTO.builder()
                .id(room.getId())
                .ibeRoomId(room.getIbeRoomId())
                .roomNumber(room.getRoomNumber())
                .checkinDate(room.getCheckinDate())
                .checkoutDate(room.getCheckoutDate())
                .occupancyStatus(room.getOccupancyStatus())
                .cleaningType(room.getCleaningType())
                .build();
    }
}