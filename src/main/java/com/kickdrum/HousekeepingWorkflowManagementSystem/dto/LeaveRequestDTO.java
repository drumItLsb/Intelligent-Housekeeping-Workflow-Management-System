package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
public class LeaveRequestDTO {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "staff_id is required")
    @JsonProperty("staff_id")
    private Long staffId;

    @NotBlank(message = "Shift is required")
    private String shift;

    @NotBlank(message = "leaveType is required")
    private String leaveType;

    private String reason;
}
