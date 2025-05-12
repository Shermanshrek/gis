package ru.gisback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class SignInRequest {
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}


