package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.LeaveRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.LeaveResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave")
public class LeaveRequestController {

    private final LeaveService leaveService;

    public LeaveRequestController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    public ResponseEntity<LeaveResponseDTO> applyLeave(@Valid @RequestBody LeaveRequestDTO request) {
        LeaveResponseDTO response = leaveService.applyLeave(request);
        return ResponseEntity.ok(response);
    }
}
