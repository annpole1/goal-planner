package com.example.goalplanner.controller;

import com.example.goalplanner.dto.GoalCreateDTO;
import com.example.goalplanner.dto.GoalResponseDTO;
import com.example.goalplanner.entity.Goal;
import com.example.goalplanner.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    // Получаем ID пользователя из токена автоматически
    private Long getCurrentUserId() {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getPrincipal().toString());
    }

    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> getAllGoals() {
        return ResponseEntity.ok(goalService.getGoalsByUser(getCurrentUserId()));
    }

    @PostMapping
    public ResponseEntity<GoalResponseDTO> createGoal(@Valid @RequestBody GoalCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(goalService.createGoal(getCurrentUserId(), dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GoalResponseDTO> updateGoalStatus(
            @PathVariable Long id,
            @RequestBody Goal.Status status) {
        return ResponseEntity.ok(goalService.updateStatus(id, getCurrentUserId(), status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id, getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}