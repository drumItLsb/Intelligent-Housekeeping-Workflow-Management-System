package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShortfallResponseDTO {

    private LocalDate date;
    private Integer extraStaffNeeded;
    private HkStaffShift shift;
    private Boolean resolved;
}
