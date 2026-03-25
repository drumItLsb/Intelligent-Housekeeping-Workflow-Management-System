package com.kickdrum.HousekeepingWorkflowManagementSystem.repository;

import com.kickdrum.HousekeepingWorkflowManagementSystem.entity.HkStaff;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HkStaffRepository extends JpaRepository<HkStaff, Long> {
    Optional<HkStaff> findByUsername(String username);
}
