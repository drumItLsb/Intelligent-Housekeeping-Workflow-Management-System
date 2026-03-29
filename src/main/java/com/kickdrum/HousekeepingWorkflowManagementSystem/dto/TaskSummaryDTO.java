package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSummaryDTO {

    private int pending;
    private int inProgress;
    private int completed;
    private int reassigned;
}
