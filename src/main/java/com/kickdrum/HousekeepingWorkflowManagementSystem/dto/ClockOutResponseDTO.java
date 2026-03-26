package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClockOutResponseDTO {

    private Long id;

    @JsonProperty("staff_id")
    private Long staffId;

    private LocalDateTime clockIn;

    private LocalDateTime clockOut;

    private String workedHours;

    private boolean earlyExit;
}
