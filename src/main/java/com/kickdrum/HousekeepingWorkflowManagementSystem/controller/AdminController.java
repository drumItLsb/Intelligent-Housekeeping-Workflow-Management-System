package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponseDTO> createStaff(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = adminService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
