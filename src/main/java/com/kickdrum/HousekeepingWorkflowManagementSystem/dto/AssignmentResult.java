package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResult {
    @Builder.Default
    private List<HkAssignment> created = new ArrayList<>();

    @Builder.Default
    private List<RoomDTO> unassignedRooms = new ArrayList<>();

    private boolean hasShortfall;
}