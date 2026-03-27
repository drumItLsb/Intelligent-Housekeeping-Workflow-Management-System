package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoomOccupancyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IbeRoomDTO {
    private UUID roomId;
    private String roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private HkRoomOccupancyStatus occupancyStatus;
}