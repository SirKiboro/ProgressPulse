package com.progresspulse.app.dto;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ProgressEntryDTO(
        Long id,
        Long entryId,
        @Positive double weightKg,      // User weight in kg
        @Positive double bodyFatPercent,    // Body fat percentage
        LocalDate date                   // Optional; service defaults to today if null
){}
