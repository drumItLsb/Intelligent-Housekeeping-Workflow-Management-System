package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.SchedulerResult;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/hk/scheduler")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    @PostMapping("/run")
    public SchedulerResult runAssignment(
            @RequestParam UUID propertyId,
            @RequestParam LocalDate date
    ) {
        return schedulerService.runAssignment(propertyId, date);
    }
}