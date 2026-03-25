package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffRole;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {

    private Long staffId;
    private String name;
    private HkStaffShift shift;
    private HkStaffRole role;
}
