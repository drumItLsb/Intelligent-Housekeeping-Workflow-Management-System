package com.kickdrum.HousekeepingWorkflowManagementSystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "hk_staff_capacity",
    uniqueConstraints = @UniqueConstraint(name = "uk_hk_staff_capacity_staff_date_shift", columnNames = {"staff_id", "date", "shift"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "staff")
public class HkStaffCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private HkStaff staff;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false, length = 10)
    private HkStaffShift shift;

    @NotNull
    @Min(0)
    @Default
    @Column(name = "allocated_minutes", nullable = false)
    private Integer allocatedMinutes = 0;

    @NotNull
    @Min(1)
    @Default
    @Column(name = "max_minutes", nullable = false)
    private Integer maxMinutes = 240;

    @PrePersist
    public void prePersist() {
        if (allocatedMinutes == null) {
            allocatedMinutes = 0;
        }
        if (maxMinutes == null) {
            maxMinutes = 240;
        }
    }
}
