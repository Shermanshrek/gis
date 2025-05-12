package ru.gisback.dto;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken
) {}
