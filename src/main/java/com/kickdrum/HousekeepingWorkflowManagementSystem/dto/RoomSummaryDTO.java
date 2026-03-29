package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomSummaryDTO {

    private int occupied;
    private int vacant;
    private int deepClean;
    private int dailyClean;
    private int vacantClean;
}
