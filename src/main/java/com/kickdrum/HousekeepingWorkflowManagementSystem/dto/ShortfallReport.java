package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortfallReport {
    private LocalDate date;

    private boolean deepCleanShortfall;
    private Integer extraStaffForDeepClean;

    private boolean dailyVacantShortfall;
    private Integer extraStaffForDailyVacant;
}