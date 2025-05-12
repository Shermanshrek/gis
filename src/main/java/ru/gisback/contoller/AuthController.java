package ru.gisback.contoller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gisback.dto.AuthResponse;
import ru.gisback.dto.SignInRequest;
import ru.gisback.dto.TokenRefreshRequest;
import ru.gisback.model.User;
import ru.gisback.services.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Аутентификация пользователя")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }


    @Operation(summary = "Обновление токенов")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshTokens(request.getRefreshToken()));
    }

    @Operation(summary = "Выход из системы")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal User user) {
        authService.logout(user.getId());
        return ResponseEntity.noContent().build();
    }
}
