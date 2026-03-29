package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeginTaskResponseDTO {
    private Long taskId;
    private String message;
    private String status;
}
