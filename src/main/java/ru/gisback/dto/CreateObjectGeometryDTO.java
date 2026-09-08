package ru.gisback.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record CreateObjectGeometryDTO(
        @NotEmpty Map<String, String> description,   // {"ru": "...", "en": "..."}
        @NotNull List<Double> points,                // [lon, lat] для точки
        int dimension                                // 0 = точка, 1 = линия, 2 = полигон
) {}
