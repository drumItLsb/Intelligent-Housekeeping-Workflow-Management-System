package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.AssignmentResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignment;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.StaffNotFoundException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkAssignmentRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffService {

    private final HkAssignmentRepository hkAssignmentRepository;
    private final HkStaffRepository hkStaffRepository;

    public StaffService(HkAssignmentRepository hkAssignmentRepository, HkStaffRepository hkStaffRepository) {
        this.hkAssignmentRepository = hkAssignmentRepository;
        this.hkStaffRepository = hkStaffRepository;
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> getAssignments(
            LocalDate date,
            HkStaffShift shift,
            Long staffId,
            String propertyId
    ) {
        HkStaff staff = hkStaffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with id: " + staffId));

        UUID propertyUuid = parsePropertyId(propertyId);

        List<HkAssignment> assignments = hkAssignmentRepository
                .findByDateAndShiftAndStaffIdAndPropertyIdOrderByAssignedAtAsc(date, shift, staffId, propertyUuid);

        if (assignments.isEmpty()) {
            return List.of();
        }

        return assignments.stream()
                .map(assignment -> mapToAssignmentResponse(assignment, staff.getId()))
                .toList();
    }

    private UUID parsePropertyId(String propertyId) {
        try {
            return UUID.fromString(propertyId);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid property_id: " + propertyId);
        }
    }

    private AssignmentResponseDTO mapToAssignmentResponse(HkAssignment assignment, Long staffId) {
        return new AssignmentResponseDTO(
                assignment.getRoom().getId(),
                staffId,
                assignment.getTaskType().name(),
                assignment.getDurationMinutes(),
                assignment.getShift().name(),
                assignment.getAssignedAt() != null ? assignment.getAssignedAt().toString() : null,
                assignment.getStatus().name()
        );
    }
}
