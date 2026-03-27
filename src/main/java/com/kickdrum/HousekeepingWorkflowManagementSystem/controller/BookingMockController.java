package com.kickdrum.HousekeepingWorkflowManagementSystem.controller;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.IbeRoomDTO;
import com.kickdrum.HousekeepingWorkflowManagementSystem.service.BookingMockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mock/bookings")
@RequiredArgsConstructor
public class BookingMockController {

    private final BookingMockService bookingMockService;

    @GetMapping
    public List<IbeRoomDTO> getBookedRooms(
            @RequestParam UUID propertyId,
            @RequestParam LocalDate date
    ) {
        return bookingMockService.getBookedRooms(propertyId, date);
    }
}