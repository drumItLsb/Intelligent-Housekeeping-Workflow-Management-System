package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignmentStatus;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignmentTaskType;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssignemntMappingResponseDTO {

    private Long staffId;
    private Long roomId;
    private HkAssignmentTaskType taskType;
    private HkAssignmentStatus status;
    private HkStaffShift shift;
    private Integer durationMinutes;
    private LocalDate date;
}
