package com.example.goalplanner.dto;

import com.example.goalplanner.entity.Goal;
import lombok.Data;
import java.time.LocalDate;

@Data
public class GoalResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Goal.Status status;

    public static GoalResponseDTO fromEntity(Goal goal) {
        GoalResponseDTO dto = new GoalResponseDTO();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescription());
        dto.setDeadline(goal.getDeadline());
        dto.setStatus(goal.getStatus());
        return dto;
    }
}