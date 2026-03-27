package com.kickdrum.HousekeepingWorkflowManagementSystem.dto;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private Long id;
    private String name;
    private UUID propertyId;
    private HkStaffShift shift;
    private Integer allocatedMinutes;
    private Integer maxMinutes;
}