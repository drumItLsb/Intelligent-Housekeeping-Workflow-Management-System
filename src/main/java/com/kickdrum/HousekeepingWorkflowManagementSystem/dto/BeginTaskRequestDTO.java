package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeginTaskRequestDTO {
    private LocalDateTime startTime;
    private Long taskId;
}
