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

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Username is required")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 255)
    private String password;

    @NotNull(message = "Role is required")
    private HkStaffRole role;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Email(message = "Email must be a valid email address")
    @Size(max = 100)
    private String email;

    @NotNull(message = "Shift is required")
    private HkStaffShift shift;

    @NotNull(message = "Availability is required")
    private HkStaffAvailability availability;

    @NotNull(message = "Employment type is required")
    private HkStaffEmploymentType employmentType;

    private UUID propertyId;
}
