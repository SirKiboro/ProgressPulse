package com.progresspulse.app.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProgressEntryDTO(
        @NotNull LocalDate date,
        @NotNull @DecimalMin("0.0") BigDecimal weight,
        @NotNull @DecimalMin("0.0") BigDecimal bodyFatPercent,
        @Size(max = 500) String notes,
        @NotNull Long userId
) {}
