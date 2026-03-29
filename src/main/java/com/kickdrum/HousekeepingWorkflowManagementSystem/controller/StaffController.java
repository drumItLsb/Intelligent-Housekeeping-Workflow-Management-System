package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.AssignmentResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.StaffService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
