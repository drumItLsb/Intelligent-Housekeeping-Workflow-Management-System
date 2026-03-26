package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String userName;
    private String role;

    public AuthResponseDTO(String token, String userName, String role) {
        this.token = token;
        this.userName = userName;
        this.role = role;
    }
}