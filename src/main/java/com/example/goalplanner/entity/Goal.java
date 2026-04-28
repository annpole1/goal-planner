package com.example.goalplanner.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Goal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false)
    private Long userId;

    public enum Status { PENDING, IN_PROGRESS, DONE, OVERDUE }
}