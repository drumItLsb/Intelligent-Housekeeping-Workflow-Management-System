package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Hello vanakkam to kickdrum hrm!");
    }
}
