package com.kickdrum.HousekeepingWorkflowManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity @Table(name = "hk_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HkRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ibe_room_id", nullable = false, columnDefinition = "uuid")
    private UUID ibeRoomId;

    @NotBlank
    @Size(max = 10)
    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "occupancy_status", nullable = false, length = 10)
    private HkRoomOccupancyStatus occupancyStatus;

    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "cleaning_type", length = 10)
    private HkRoomCleaningType cleaningType;

    @Enumerated(EnumType.STRING)
    @Column(name = "assigned_shift", length = 10)
    private HkStaffShift assignedShift;

    @Default
    @Column(name = "is_cleaned")
    private Boolean isCleaned = false;

    @Column(name = "last_cleaned_at")
    private LocalDateTime lastCleanedAt;

    @PrePersist
    public void prePersist() {
        if (isCleaned == null) {
            isCleaned = false;
        }
    }
}