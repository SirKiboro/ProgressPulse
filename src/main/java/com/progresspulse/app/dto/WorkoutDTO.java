package com.progresspulse.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record WorkoutDTO(
        @NotBlank String type,             // e.g., "Push-ups", "Running"
        @Positive int durationMinutes,     // Duration in minutes
        @Positive int caloriesBurned,      // Calories burned
        LocalDate date                     // Optional; default to today in service if null
) {}


