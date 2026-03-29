package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.AssignemntMappingResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.SummaryResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.AdminService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SummaryResponseDTO> fetchSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam HkStaffShift shift
    ) {
        SummaryResponseDTO response = adminService.fetchSummary(date, shift);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/assignment/mapping")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AssignemntMappingResponseDTO>> getAssignmentMapping(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam HkStaffShift shift,
            @RequestParam UUID propertyId
    ) {
        List<AssignemntMappingResponseDTO> response = adminService.getAssignmentMapping(date, shift, propertyId);
        return ResponseEntity.ok(response);
    }
}
