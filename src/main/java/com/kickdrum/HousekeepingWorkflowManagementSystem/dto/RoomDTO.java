package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoomCleaningType;
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
public class RoomDTO {
    private Long id;
    private UUID ibeRoomId;
    private String roomNumber;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private HkRoomOccupancyStatus occupancyStatus;
    private HkRoomCleaningType cleaningType;
}