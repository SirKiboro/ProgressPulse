package com.progresspulse.app.dto;

import com.progresspulse.app.model.ExerciseType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record WorkoutDTO(
        @NotNull LocalDate date,
        @NotNull ExerciseType exerciseType,
        @Min(1) int durationMinutes,
        @NotNull @DecimalMin("0.0") BigDecimal caloriesBurned,
        @Size(max = 500) String notes,
        @NotNull Long userId
) {}
