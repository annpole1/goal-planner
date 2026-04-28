package com.example.goalplanner;

import com.example.goalplanner.dto.GoalCreateDTO;
import com.example.goalplanner.dto.GoalResponseDTO;
import com.example.goalplanner.entity.Goal;
import com.example.goalplanner.repository.GoalRepository;
import com.example.goalplanner.service.GoalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Включаем поддержку Mockito
class GoalServiceTest {

    @Mock
    private GoalRepository repository; // Имитация базы данных

    @InjectMocks
    private GoalService goalService;   // Реальный сервис, который будем тестировать

    @Test
    void createGoal_Success() {
        // 1. Подготовка данных (Arrange)
        GoalCreateDTO dto = new GoalCreateDTO();
        dto.setTitle("Test Goal");
        dto.setDescription("Testing...");
        // Ставим дату в будущем, чтобы статус стал PENDING
        dto.setDeadline(LocalDate.now().plusDays(5));

        // Имитируем ответ от "базы данных"
        Goal savedGoal = new Goal();
        savedGoal.setId(1L);
        savedGoal.setTitle("Test Goal");
        savedGoal.setStatus(Goal.Status.PENDING);
        savedGoal.setUserId(123L); // В реальном приложении ID берется из токена

        // Когда вызовут repo.save(any()), вернуть наш savedGoal
        when(repository.save(any(Goal.class))).thenReturn(savedGoal);

        // 2. Вызов метода (Act)
        GoalResponseDTO result = goalService.createGoal(123L, dto);

        // 3. Проверка (Assert)
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Goal", result.getTitle());
        assertEquals(Goal.Status.PENDING, result.getStatus());

        // Проверяем, что метод save был вызван 1 раз
        verify(repository, times(1)).save(any(Goal.class));
    }

    @Test
    void createGoal_Overdue() {
        // Тест для проверки статуса OVERDUE (если дедлайн в прошлом)
        GoalCreateDTO dto = new GoalCreateDTO();
        dto.setTitle("Late Goal");
        dto.setDeadline(LocalDate.now().minusDays(1)); // Вчера

        Goal savedGoal = new Goal();
        savedGoal.setId(2L);
        savedGoal.setStatus(Goal.Status.OVERDUE);

        when(repository.save(any(Goal.class))).thenReturn(savedGoal);

        GoalResponseDTO result = goalService.createGoal(123L, dto);

        assertEquals(Goal.Status.OVERDUE, result.getStatus());
    }
}