package ru.gisback.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gisback.dto.*;
import ru.gisback.model.RefreshToken;
import ru.gisback.model.Role;
import ru.gisback.model.User;
import ru.gisback.repositories.RefreshTokenRepo;
import ru.gisback.repositories.UserRepo;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepo userRepository;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role role = Role.valueOf(request.getRole());

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
        return generateAndSaveTokens(user);
    }

    @Transactional
    public AuthResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new SecurityException("Invalid credentials");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return generateAndSaveTokens(user);
    }

    @Transactional
    public AuthResponse refreshTokens(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new SecurityException("Invalid refresh token"));

        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepo.delete(storedToken);
            throw new SecurityException("Refresh token expired");
        }

        User user = storedToken.getUser();
        refreshTokenRepo.delete(storedToken);

        return generateAndSaveTokens(user);
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepo.deleteByUser_Id(userId);
    }

    private AuthResponse generateAndSaveTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken);

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtService.getAccessExpiration(),
                jwtService.getRefreshExpiration()
        );
    }

    @Transactional
    protected void saveRefreshToken(User user, String tokenValue) {
        RefreshToken rt = refreshTokenRepo.findByUser_Id(user.getId())
                .orElseGet(RefreshToken::new);

        rt.setUser(user);                       // если новый
        rt.setToken(tokenValue);
        rt.setExpiryDate(Instant.now()
                .plusMillis(jwtService.getRefreshExpiration()));

        refreshTokenRepo.save(rt);              // будет INSERT или UPDATE
    }

}