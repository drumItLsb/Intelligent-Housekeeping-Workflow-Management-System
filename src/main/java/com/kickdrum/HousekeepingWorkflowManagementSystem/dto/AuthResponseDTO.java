package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String userName;
    private String role;
    private Long staffId;
    private UUID propertyId;
    private HkStaffShift shift;

    public AuthResponseDTO(String token, String userName, String role, Long staffId, UUID propertyId, HkStaffShift shift) {
        this.token = token;
        this.userName = userName;
        this.role = role;
        this.staffId = staffId;
        this.propertyId = propertyId;
        this.shift = shift;
    }
}