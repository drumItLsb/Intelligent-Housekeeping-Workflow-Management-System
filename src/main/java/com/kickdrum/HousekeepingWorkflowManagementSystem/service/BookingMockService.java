package com.kickdrum.HousekeepingWorkflowManagementSystem.service;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.IbeRoomDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoomOccupancyStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingMockService {

    public List<IbeRoomDTO> getBookedRooms(UUID propertyId, LocalDate date) {
        List<IbeRoomDTO> rooms = new ArrayList<>();

        rooms.add(IbeRoomDTO.builder()
                .roomId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .roomNumber("101")
                .checkIn(date.minusDays(1))
                .checkOut(date)
                .occupancyStatus(HkRoomOccupancyStatus.OCCUPIED)
                .build());

        rooms.add(IbeRoomDTO.builder()
                .roomId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .roomNumber("102")
                .checkIn(date.minusDays(2))
                .checkOut(date)
                .occupancyStatus(HkRoomOccupancyStatus.OCCUPIED)
                .build());

        rooms.add(IbeRoomDTO.builder()
                .roomId(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                .roomNumber("103")
                .checkIn(date)
                .checkOut(date.plusDays(1))
                .occupancyStatus(HkRoomOccupancyStatus.OCCUPIED)
                .build());

        rooms.add(IbeRoomDTO.builder()
                .roomId(UUID.fromString("44444444-4444-4444-4444-444444444444"))
                .roomNumber("104")
                .checkIn(date)
                .checkOut(date.plusDays(3))
                .occupancyStatus(HkRoomOccupancyStatus.VACANT)
                .build());

        return rooms;
    }
}
