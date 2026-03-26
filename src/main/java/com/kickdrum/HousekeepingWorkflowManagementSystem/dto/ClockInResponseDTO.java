package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClockInResponseDTO {

    private Long attendanceId;
    private Long staffId;
    private LocalDateTime clockIn;
    private HkStaffShift shift;
    private String availability;
}
