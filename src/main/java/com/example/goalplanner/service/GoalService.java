package com.example.goalplanner.service;

import com.example.goalplanner.dto.GoalCreateDTO;
import com.example.goalplanner.dto.GoalResponseDTO;
import com.example.goalplanner.entity.Goal;
import com.example.goalplanner.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository repository;

    public List<GoalResponseDTO> getGoalsByUser(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(GoalResponseDTO::fromEntity)
                .toList();
    }

    public GoalResponseDTO createGoal(Long userId, GoalCreateDTO dto) {
        Goal goal = new Goal();
        goal.setTitle(dto.getTitle());
        goal.setDescription(dto.getDescription());
        goal.setDeadline(dto.getDeadline());
        goal.setUserId(userId);
        goal.setStatus(LocalDate.now().isAfter(dto.getDeadline())
                ? Goal.Status.OVERDUE
                : Goal.Status.PENDING);

        Goal saved = repository.save(goal);
        return GoalResponseDTO.fromEntity(saved);
    }

    public GoalResponseDTO updateStatus(Long goalId, Long userId, Goal.Status newStatus) {
        Goal goal = repository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        goal.setStatus(newStatus);
        return GoalResponseDTO.fromEntity(repository.save(goal));
    }

    public void deleteGoal(Long goalId, Long userId) {
        Goal goal = repository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        repository.delete(goal);
    }
}