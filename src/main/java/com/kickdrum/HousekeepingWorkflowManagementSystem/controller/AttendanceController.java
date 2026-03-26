package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockInRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockInResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockOutRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockOutResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/clockin")
    public ResponseEntity<ClockInResponseDTO> checkIn(@Valid @RequestBody ClockInRequestDTO request) {
        ClockInResponseDTO response = attendanceService.clockIn(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/clockout")
    public ResponseEntity<ClockOutResponseDTO> clockOut(@Valid @RequestBody ClockOutRequestDTO request) {
        ClockOutResponseDTO response = attendanceService.clockOut(request);
        return ResponseEntity.ok(response);
    }
}
