package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SummaryResponseDTO {

    private LocalDate date;
    private StaffSummaryDTO staffSummary;
    private RoomSummaryDTO roomSummary;
    private TaskSummaryDTO taskSummary;
}
