package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffAvailability;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HkStaffRepository extends JpaRepository<HkStaff, Long> {
    Optional<HkStaff> findByUsername(String username);
    long countByAvailability(HkStaffAvailability availability);
    List<HkStaff> findByPropertyIdAndShiftAndAvailability(
            UUID propertyId,
            HkStaffShift shift,
            HkStaffAvailability availability
    );
}
