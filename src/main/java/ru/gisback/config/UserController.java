package ru.gisback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gisback.dto.UserDTO;
import ru.gisback.model.User;
import ru.gisback.services.UserService;

@RestController
@RequestMapping("/api/user/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(userService.getUserDTO(user));
    }
}


