package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerResult {
    private LocalDate date;
    private int morningAssigned;
    private int afternoonAssigned;

    @Builder.Default
    private List<RoomDTO> unassignedRooms = new ArrayList<>();

    private ShortfallReport earlyWarning;
}