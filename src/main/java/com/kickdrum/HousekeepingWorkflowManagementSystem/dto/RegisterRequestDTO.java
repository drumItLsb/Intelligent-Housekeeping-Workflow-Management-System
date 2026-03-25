package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffAvailability;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffEmploymentType;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffRole;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;

    @NotNull
    private HkStaffRole role;

    @Size(max = 20)
    private String phone;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    private HkStaffShift shift;

    @NotNull
    private HkStaffAvailability availability;

    @NotNull
    private HkStaffEmploymentType employmentType;

    private UUID propertyId;
}
