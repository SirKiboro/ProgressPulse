package com.progresspulse.app.dto;

import com.progresspulse.app.model.Gender;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO(
        @NotBlank @Size(min = 2, max = 100) String name,
        @NotBlank @Email @Size(max = 255) String email,
        @NotNull @Past LocalDate dateOfBirth,
        @NotNull @DecimalMin("50.0") @DecimalMax("300.0") BigDecimal heightCm,
        @NotNull Gender gender
) {}
