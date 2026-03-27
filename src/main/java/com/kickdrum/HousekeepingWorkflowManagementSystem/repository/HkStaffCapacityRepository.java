package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffCapacity;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HkStaffCapacityRepository extends JpaRepository<HkStaffCapacity, Long> {

    Optional<HkStaffCapacity> findByStaffAndDateAndShift(
            HkStaff staff,
            LocalDate date,
            HkStaffShift shift
    );
}