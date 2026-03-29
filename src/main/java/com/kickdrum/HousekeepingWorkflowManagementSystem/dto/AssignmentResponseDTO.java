package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseDTO {

    private Long roomId;
    private Long staffId;
    private String taskType;
    private Integer durationMinutes;
    private String shift;
    private String assignedAt;
    private String status;
}
