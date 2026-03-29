package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StaffSummaryDTO {

    private int onDuty;
    private int onLeave;
    private int sick;
}
