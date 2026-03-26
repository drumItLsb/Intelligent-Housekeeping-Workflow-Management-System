package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClockInRequestDTO {

    @NotNull(message = "Id is required")
    private Long id;

    @NotNull(message = "Shift is required")
    private HkStaffShift shift;
}
