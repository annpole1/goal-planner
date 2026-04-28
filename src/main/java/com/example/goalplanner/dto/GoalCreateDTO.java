package com.example.goalplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class GoalCreateDTO {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private LocalDate deadline;
}