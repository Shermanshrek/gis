package ru.gisback.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gisback.dto.JwtAuthenticationResponse;
import ru.gisback.dto.SignInRequest;
import ru.gisback.dto.SignUpRequest;
import ru.gisback.services.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        try {
            return ResponseEntity.ok().body(authenticationService.signIn(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        try {
            return ResponseEntity.ok().body(authenticationService.signUp(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
