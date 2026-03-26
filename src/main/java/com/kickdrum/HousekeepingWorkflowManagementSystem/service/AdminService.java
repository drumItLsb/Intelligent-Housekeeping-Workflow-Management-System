package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.RegisterResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.exception.ResourceAlreadyExistsException;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final HkStaffRepository hkStaffRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(HkStaffRepository hkStaffRepository, PasswordEncoder passwordEncoder) {
        this.hkStaffRepository = hkStaffRepository;
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
}
