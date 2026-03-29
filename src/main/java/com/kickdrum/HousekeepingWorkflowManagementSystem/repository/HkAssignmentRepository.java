package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignment;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignmentStatus;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HkAssignmentRepository extends JpaRepository<HkAssignment, Long> {
    List<HkAssignment> findByDateAndShiftAndStaffIdAndPropertyIdOrderByAssignedAtAsc(
            LocalDate date,
            HkStaffShift shift,
            Long staffId,
            UUID propertyId
    );

    long countByDateAndShiftAndStatus(
            LocalDate date,
            HkStaffShift shift,
            HkAssignmentStatus status
    );
}
