package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAttendance;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HkAttendanceRepository extends JpaRepository<HkAttendance, Long> {
    boolean existsByStaffAndDateAndShiftAndClockInIsNotNull(HkStaff staff, LocalDate date, HkStaffShift shift);

    Optional<HkAttendance> findByStaffIdAndDateAndShift(Long staffId, LocalDate date, HkStaffShift shift);
}
