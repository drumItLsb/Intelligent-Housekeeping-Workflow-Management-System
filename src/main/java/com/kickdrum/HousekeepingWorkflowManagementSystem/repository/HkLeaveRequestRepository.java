package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkLeaveRequest;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveType;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveRequestStatus;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveShift;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HkLeaveRequestRepository extends JpaRepository<HkLeaveRequest, Long> {
    boolean existsByStaffAndDateAndShiftAndStatus(
            HkStaff staff,
            LocalDate date,
            LeaveShift shift,
            LeaveRequestStatus status
    );

    long countByDateAndLeaveType(
            LocalDate date,
            LeaveType leaveType
    );
}
