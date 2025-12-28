package com.progresspulse.app.dto;

import com.progresspulse.app.model.MealType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MealDTO(
        @NotNull LocalDate date,
        @NotNull MealType mealType,
        @NotNull @DecimalMin("0.0") BigDecimal protein,
        @NotNull @DecimalMin("0.0") BigDecimal carbs,
        @NotNull @DecimalMin("0.0") BigDecimal fats,
        @NotNull @DecimalMin("0.0") BigDecimal calories,
        @Size(max = 500) String foodItems,
        @NotNull Long userId
) {}
