package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.AssignemntMappingResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RoomSummaryDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ShortfallResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.StaffSummaryDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.SummaryResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.TaskSummaryDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignment;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAssignmentStatus;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoomCleaningType;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoomOccupancyStatus;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkShortfallAlert;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffAvailability;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaffShift;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveShift;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.LeaveType;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.AssignmentMappingFetchException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.ResourceAlreadyExistsException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.ShortfallFetchException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.SummaryFetchException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkAssignmentRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkLeaveRequestRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkRoomRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkShortfallAlertRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final HkStaffRepository hkStaffRepository;
    private final HkLeaveRequestRepository hkLeaveRequestRepository;
    private final HkRoomRepository hkRoomRepository;
    private final HkAssignmentRepository hkAssignmentRepository;
    private final HkShortfallAlertRepository hkShortfallAlertRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            HkStaffRepository hkStaffRepository,
            HkLeaveRequestRepository hkLeaveRequestRepository,
            HkRoomRepository hkRoomRepository,
            HkAssignmentRepository hkAssignmentRepository,
            HkShortfallAlertRepository hkShortfallAlertRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.hkStaffRepository = hkStaffRepository;
        this.hkLeaveRequestRepository = hkLeaveRequestRepository;
        this.hkRoomRepository = hkRoomRepository;
        this.hkAssignmentRepository = hkAssignmentRepository;
        this.hkShortfallAlertRepository = hkShortfallAlertRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        hkStaffRepository.findByUsername(request.getUsername()).ifPresent(existing -> {
            throw new ResourceAlreadyExistsException("Username already exists: " + request.getUsername());
        });

        HkStaff staff = HkStaff.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .phone(request.getPhone())
                .email(request.getEmail())
                .shift(request.getShift())
                .availability(request.getAvailability())
                .employmentType(request.getEmploymentType())
                .propertyId(request.getPropertyId())
                .build();

        HkStaff saved = hkStaffRepository.save(staff);

        return new RegisterResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getShift(),
                saved.getRole()
        );
    }

    public HkStaff getByUserName(String userName) {
        return hkStaffRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
    }

    public SummaryResponseDTO fetchSummary(LocalDate date, HkStaffShift shift) {
        try {
            LeaveShift leaveShift = LeaveShift.valueOf(shift.name());

            StaffSummaryDTO staffSummary = new StaffSummaryDTO();
            staffSummary.setOnDuty(toInt(hkStaffRepository.countByShiftAndAvailability(
                    shift,
                    HkStaffAvailability.ON_DUTY
            )));
            staffSummary.setOnLeave(toInt(hkLeaveRequestRepository.countByDateAndShiftAndLeaveType(
                    date,
                    leaveShift,
                    LeaveType.PLANNED
            )));
            staffSummary.setSick(toInt(hkLeaveRequestRepository.countByDateAndShiftAndLeaveType(
                    date,
                    leaveShift,
                    LeaveType.SICK
            )));

            RoomSummaryDTO roomSummary = new RoomSummaryDTO();
            roomSummary.setOccupied(toInt(hkRoomRepository.countByCheckoutDateAndOccupancyStatus(
                    date,
                    HkRoomOccupancyStatus.OCCUPIED
            )));
            roomSummary.setVacant(toInt(hkRoomRepository.countByCheckoutDateAndOccupancyStatus(
                    date,
                    HkRoomOccupancyStatus.VACANT
            )));
            roomSummary.setDeepClean(toInt(hkRoomRepository.countByCheckoutDateAndOccupancyStatusAndCleaningType(
                    date,
                    HkRoomOccupancyStatus.OCCUPIED,
                    HkRoomCleaningType.DEEP
            )));
            roomSummary.setDailyClean(toInt(hkRoomRepository.countByCheckoutDateAndOccupancyStatusAndCleaningType(
                    date,
                    HkRoomOccupancyStatus.OCCUPIED,
                    HkRoomCleaningType.DAILY
            )));
            roomSummary.setVacantClean(toInt(hkRoomRepository.countByCheckoutDateAndOccupancyStatusAndCleaningType(
                    date,
                    HkRoomOccupancyStatus.VACANT,
                    HkRoomCleaningType.VACANT
            )));

            TaskSummaryDTO taskSummary = new TaskSummaryDTO();
            taskSummary.setPending(toInt(hkAssignmentRepository.countByDateAndShiftAndStatus(
                    date,
                    shift,
                    HkAssignmentStatus.PENDING
            )));
            taskSummary.setInProgress(toInt(hkAssignmentRepository.countByDateAndShiftAndStatus(
                    date,
                    shift,
                    HkAssignmentStatus.IN_PROGRESS
            )));
            taskSummary.setCompleted(toInt(hkAssignmentRepository.countByDateAndShiftAndStatus(
                    date,
                    shift,
                    HkAssignmentStatus.COMPLETED
            )));
            taskSummary.setReassigned(toInt(hkAssignmentRepository.countByDateAndShiftAndStatus(
                    date,
                    shift,
                    HkAssignmentStatus.REASSIGNED
            )));

            SummaryResponseDTO response = new SummaryResponseDTO();
            response.setDate(date);
            response.setShift(shift.name());
            response.setStaffSummary(staffSummary);
            response.setRoomSummary(roomSummary);
            response.setTaskSummary(taskSummary);
            return response;
        } catch (Exception ex) {
            throw new SummaryFetchException("Failed to fetch summary", ex);
        }
    }

    public List<AssignemntMappingResponseDTO> getAssignmentMapping(
            LocalDate date,
            HkStaffShift shift,
            UUID propertyId
    ) {
        try {
            return hkAssignmentRepository.findByDateAndShiftAndPropertyIdOrderByAssignedAtAsc(date, shift, propertyId)
                    .stream()
                    .map(this::mapToAssignmentMappingResponse)
                    .toList();
        } catch (Exception ex) {
            throw new AssignmentMappingFetchException("Failed to fetch assignment mapping", ex);
        }
    }

    public ShortfallResponseDTO getShortfall(LocalDate date) {
        try {
            HkShortfallAlert shortfallAlert = hkShortfallAlertRepository.findFirstByDateOrderByCreatedAtDesc(date)
                    .orElseThrow(() -> new ShortfallFetchException("Shortfall not found for date: " + date));

            ShortfallResponseDTO response = new ShortfallResponseDTO();
            response.setDate(shortfallAlert.getDate());
            response.setExtraStaffNeeded(shortfallAlert.getExtraStaffNeeded());
            response.setShift(shortfallAlert.getShift());
            response.setResolved(shortfallAlert.getResolved());
            return response;
        } catch (ShortfallFetchException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ShortfallFetchException("Failed to fetch shortfall", ex);
        }
    }

    private int toInt(long value) {
        return Math.toIntExact(value);
    }

    private AssignemntMappingResponseDTO mapToAssignmentMappingResponse(HkAssignment assignment) {
        AssignemntMappingResponseDTO response = new AssignemntMappingResponseDTO();
        response.setStaffId(assignment.getStaff() != null ? assignment.getStaff().getId() : null);
        response.setRoomId(assignment.getRoom() != null ? assignment.getRoom().getId() : null);
        response.setTaskType(assignment.getTaskType());
        response.setStatus(assignment.getStatus());
        response.setShift(assignment.getShift());
        response.setDurationMinutes(assignment.getDurationMinutes());
        response.setDate(assignment.getDate());
        return response;
    }
}
