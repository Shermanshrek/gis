package ru.gisback.contoller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.gisback.dto.AuthResponse;
import ru.gisback.dto.RoleUpdateDTO;
import ru.gisback.dto.SignUpRequest;
import ru.gisback.dto.UserDTO;
import ru.gisback.model.Role;
import ru.gisback.services.AuthService;
import ru.gisback.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "Получить всех пользователей")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Изменить роль пользователя")
    @PutMapping("/users/{id}/role")                        // ← PUT
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestBody @Valid RoleUpdateDTO dto) {       // ← JSON‑тело
        userService.updateUserRole(id, dto.role());
        return ResponseEntity.noContent().build();         // 204 No Content
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
