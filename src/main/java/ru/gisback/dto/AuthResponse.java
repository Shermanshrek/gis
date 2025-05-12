package ru.gisback.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        long accessExpiresIn,
        long refreshExpiresIn
) {}


