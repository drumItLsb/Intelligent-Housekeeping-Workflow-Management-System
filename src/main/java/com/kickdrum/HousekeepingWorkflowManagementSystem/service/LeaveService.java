package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.LeaveRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.LeaveResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkLeaveRequest;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveRequestStatus;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveShift;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveType;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.LeaveRequestAlreadyExists;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.ShortLeaveRequestException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkLeaveRequestRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveService {

    private static final String SHORT_LEAVE_MESSAGE = "Should apply leave before 2 days prior";

    private final HkLeaveRequestRepository hkLeaveRequestRepository;
    private final HkStaffRepository hkStaffRepository;

    public LeaveService(HkLeaveRequestRepository hkLeaveRequestRepository, HkStaffRepository hkStaffRepository) {
        this.hkLeaveRequestRepository = hkLeaveRequestRepository;
        this.hkStaffRepository = hkStaffRepository;
    }

    @Transactional
    public LeaveResponseDTO applyLeave(LeaveRequestDTO request) {
        HkStaff staff = hkStaffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + request.getStaffId()));

        LeaveShift shift = parseLeaveShift(request.getShift());
        LeaveType leaveType = parseLeaveType(request.getLeaveType());
        LocalDateTime reportedAt = LocalDateTime.now();

        boolean approvedLeaveExists = hkLeaveRequestRepository.existsByStaffAndDateAndShiftAndStatus(
                staff,
                request.getDate(),
                shift,
                LeaveRequestStatus.APPROVED
        );

        if (approvedLeaveExists) {
            throw new LeaveRequestAlreadyExists(
                    "Leave request already exists for staff_id " + request.getStaffId()
                            + ", date " + request.getDate()
                            + " and shift " + shift
            );
        }

        LeaveRequestStatus status = isEligibleForApproval(request.getDate())
                ? LeaveRequestStatus.APPROVED
                : LeaveRequestStatus.REJECTED;

        HkLeaveRequest leaveRequest = HkLeaveRequest.builder()
                .staff(staff)
                .date(request.getDate())
                .shift(shift)
                .leaveType(leaveType)
                .reason(request.getReason())
                .reportedAt(reportedAt)
                .status(status)
                .build();

        HkLeaveRequest savedLeaveRequest = hkLeaveRequestRepository.save(leaveRequest);

        if (savedLeaveRequest.getStatus() == LeaveRequestStatus.REJECTED) {
            throw new ShortLeaveRequestException(SHORT_LEAVE_MESSAGE);
        }

        return new LeaveResponseDTO(
                savedLeaveRequest.getStaff().getId(),
                savedLeaveRequest.getDate(),
                savedLeaveRequest.getShift().name(),
                savedLeaveRequest.getLeaveType().name(),
                savedLeaveRequest.getStatus().name()
        );
    }

    private boolean isEligibleForApproval(LocalDate leaveDate) {
        return !leaveDate.isBefore(LocalDate.now().plusDays(2));
    }

    private LeaveShift parseLeaveShift(String shift) {
        try {
            return LeaveShift.valueOf(shift.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid shift value: " + shift);
        }
    }

    private LeaveType parseLeaveType(String leaveType) {
        try {
            return LeaveType.valueOf(leaveType.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid leaveType value: " + leaveType);
        }
    }
}
