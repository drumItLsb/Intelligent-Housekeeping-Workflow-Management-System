package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;


import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkShortfallAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HkShortfallAlertRepository extends JpaRepository<HkShortfallAlert, Long> {
}