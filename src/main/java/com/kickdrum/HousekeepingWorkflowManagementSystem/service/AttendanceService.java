package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockInRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockInResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockOutRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ClockOutResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkAttendance;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.StaffAlreadyClockedInException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.StaffNotClockedInException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkAttendanceRepository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {

    private final HkAttendanceRepository hkAttendanceRepository;
    private final HkStaffRepository hkStaffRepository;

    public AttendanceService(HkAttendanceRepository hkAttendanceRepository, HkStaffRepository hkStaffRepository) {
        this.hkAttendanceRepository = hkAttendanceRepository;
        this.hkStaffRepository = hkStaffRepository;
    }

    @Transactional
    public ClockInResponseDTO clockIn(ClockInRequestDTO request) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        HkStaff staff = hkStaffRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + request.getId()));

        boolean alreadyClockedIn = hkAttendanceRepository.existsByStaffAndDateAndShiftAndClockInIsNotNull(
                staff,
                today,
                request.getShift()
        );

        if (alreadyClockedIn) {
            throw new StaffAlreadyClockedInException(
                    "Staff has already clocked in for " + today + " and shift " + request.getShift()
            );
        }

        HkAttendance attendance = HkAttendance.builder()
                .staff(staff)
                .date(today)
                .shift(request.getShift())
                .clockIn(now)
                .build();

        HkAttendance savedAttendance = hkAttendanceRepository.save(attendance);

        return new ClockInResponseDTO(
                savedAttendance.getId(),
                staff.getId(),
                savedAttendance.getClockIn(),
                savedAttendance.getShift(),
                "on_duty"
        );
    }

    @Transactional
    public ClockOutResponseDTO clockOut(ClockOutRequestDTO request) {
        HkAttendance attendance = hkAttendanceRepository.findByStaffIdAndDateAndShift(
                        request.getStaffId(),
                        request.getDate(),
                        request.getShift()
                )
                .orElseThrow(() -> new StaffNotClockedInException(
                        "Staff has not clocked in for " + request.getDate() + " and shift " + request.getShift()
                ));

        LocalDateTime now = LocalDateTime.now();
        attendance.setClockOut(now);

        HkAttendance savedAttendance = hkAttendanceRepository.save(attendance);
        String workedHours = calculateWorkedHours(savedAttendance.getClockIn(), savedAttendance.getClockOut());
        boolean earlyExit = workedHours.charAt(1) != '4';

        return new ClockOutResponseDTO(
                savedAttendance.getId(),
                savedAttendance.getStaff().getId(),
                savedAttendance.getClockIn(),
                savedAttendance.getClockOut(),
                workedHours,
                earlyExit
        );
    }

    private String calculateWorkedHours(LocalDateTime clockIn, LocalDateTime clockOut) {
        Duration duration = Duration.between(clockIn, clockOut);

        long totalMinutes = duration.toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return String.format("%02d:%02d", hours, minutes);
    }
}
