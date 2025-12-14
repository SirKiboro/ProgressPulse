package com.progresspulse.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record MealDTO(@NotBlank String name,         // e.g., "Chicken, beef, beans" etc
                      @Positive int protein,         // in grams
                      @Positive int carbs,           // in grams
                      @Positive int fats,            // in grams
                      LocalDate date                 // Optional; service defaults to today if null
) {}
