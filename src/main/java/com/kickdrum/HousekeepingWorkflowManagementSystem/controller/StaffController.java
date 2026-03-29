package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.AssignmentResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.BeginTaskRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.BeginTaskResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.CompleteTaskRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.CompleteTaskResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.StaffService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignments(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("shift") HkStaffShift shift,
            @RequestParam("staff_id") Long staffId,
            @RequestParam("property_id") String propertyId
    ) {
        List<AssignmentResponseDTO> response = staffService.getAssignments(date, shift, staffId, propertyId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/task/begin")
    public ResponseEntity<BeginTaskResponseDTO> beginTask(@Valid @RequestBody BeginTaskRequestDTO request) {
        BeginTaskResponseDTO response = staffService.beginTask(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/task/complete")
    public ResponseEntity<CompleteTaskResponseDTO> completeTask(@Valid @RequestBody CompleteTaskRequestDTO request) {
        CompleteTaskResponseDTO response = staffService.completeTask(request);
        return ResponseEntity.ok(response);
    }
}
