package com.blackerp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank String name,
        @Size(min = 8) String password
) {}
