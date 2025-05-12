package ru.gisback.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateObjectGeometryDTO(
        @NotNull String description,
        @NotNull List<Double> points,   // [lon, lat] для точки
        int dimension                   // 0 = точка, 1 = линия, 2 = полигон
) {}
