package com.kickdrum.HousekeepingWorkflowManagementSystem.service;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.repository.HkStaffRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final HkStaffRepository hkStaffRepository;

    public CustomUserDetailsService(HkStaffRepository hkStaffRepository) {
        this.hkStaffRepository = hkStaffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String userName) throws UsernameNotFoundException {
        HkStaff staff = hkStaffRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));

        return new org.springframework.security.core.userdetails.User(
                staff.getUsername(),
                staff.getPassword(),
                List.of(new SimpleGrantedAuthority(staff.getRole().name()))
        );
    }
}