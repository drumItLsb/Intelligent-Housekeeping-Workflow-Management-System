package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoom;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoomCleaningType;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoomOccupancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface HkRoomRepository extends JpaRepository<HkRoom, Long> {
    Optional<HkRoom> findByIbeRoomId(UUID ibeRoomId);
    long countByCheckoutDateAndOccupancyStatus(LocalDate checkoutDate, HkRoomOccupancyStatus occupancyStatus);
    long countByCheckoutDateAndOccupancyStatusAndCleaningType(
            LocalDate checkoutDate,
            HkRoomOccupancyStatus occupancyStatus,
            HkRoomCleaningType cleaningType
    );
}
