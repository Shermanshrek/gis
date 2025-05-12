package ru.gisback.dto;

import jakarta.validation.constraints.NotNull;
import ru.gisback.model.Role;

public record RoleUpdateDTO(@NotNull Role role) {}

