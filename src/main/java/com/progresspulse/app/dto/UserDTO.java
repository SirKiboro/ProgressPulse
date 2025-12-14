package com.progresspulse.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank @Size(min = 2, max = 50) String name,
        @Email String email
) {}
