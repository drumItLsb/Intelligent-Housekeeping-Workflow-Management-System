package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;


import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkShortfallAlert;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HkShortfallAlertRepository extends JpaRepository<HkShortfallAlert, Long> {
    Optional<HkShortfallAlert> findFirstByDateOrderByCreatedAtDesc(LocalDate date);
}
