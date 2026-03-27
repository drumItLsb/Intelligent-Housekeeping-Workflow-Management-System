package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;
import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HkRoomRepository extends JpaRepository<HkRoom, Long> {
    Optional<HkRoom> findByIbeRoomId(UUID ibeRoomId);
}