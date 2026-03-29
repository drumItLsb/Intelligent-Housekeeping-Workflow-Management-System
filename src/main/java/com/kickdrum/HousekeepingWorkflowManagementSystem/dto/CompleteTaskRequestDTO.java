package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompleteTaskRequestDTO {
    private LocalDateTime completedTime;
    private Long taskId;
}
