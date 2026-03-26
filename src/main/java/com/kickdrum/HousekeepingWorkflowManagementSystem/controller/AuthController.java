package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.config.JwtService;
import com.kickdrum.HousekeepingWorkflowManagementSystem.config.TokenBlacklist;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.AuthResponseDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.LoginRequestDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminService adminService;
    private final TokenBlacklist tokenBlacklist;


    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, AdminService adminService, TokenBlacklist tokenBlacklist) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.adminService = adminService;
        this.tokenBlacklist = tokenBlacklist;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal.getUsername(), principal.getAuthorities());

        HkStaff staff = adminService.getByUserName(principal.getUsername());

        return new AuthResponseDTO(
                token,
                staff.getUsername(),
                staff.getRole().name()
        );
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.add(token);
        }

        return "Logged out successfully";
    }
}