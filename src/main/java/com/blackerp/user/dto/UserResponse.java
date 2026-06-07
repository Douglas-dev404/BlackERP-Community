package com.blackerp.user.dto;

import java.time.LocalDateTime;

import com.blackerp.user.entity.Role;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        boolean active,
        LocalDateTime createdAt
) {}
