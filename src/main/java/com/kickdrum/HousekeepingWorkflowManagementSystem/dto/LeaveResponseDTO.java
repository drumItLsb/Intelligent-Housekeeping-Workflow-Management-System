package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveResponseDTO {

    @JsonProperty("staff_id")
    private Long staffId;

    private LocalDate date;
    private String shift;
    private String leaveType;
    private String status;
}
