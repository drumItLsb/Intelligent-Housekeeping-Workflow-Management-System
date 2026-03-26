package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClockOutRequestDTO {

    @NotNull(message = "Staff id is required")
    @JsonProperty("staff_id")
    private Long staffId;

    @NotNull(message = "Shift is required")
    private HkStaffShift shift;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
