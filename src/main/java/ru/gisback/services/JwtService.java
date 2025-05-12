package ru.gisback.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.gisback.model.User;
import ru.gisback.model.Role;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
@Data
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Getter
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // Генерация access токена
    public String generateAccessToken(UserDetails userDetails) {
        if (!(userDetails instanceof User user)) {
            throw new IllegalArgumentException("Invalid user type");
        }
        validateUser(user);
        return buildAccessToken(user);
    }

    // Генерация refresh токена
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .claim("token_id", UUID.randomUUID().toString())
                .claim("type", "refresh")
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    private String buildAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        claims.put("role", user.getRole().name());
        claims.put("user_id", user.getId());

        // Добавляем информацию о слоях, если необходимо
        if(user.getLayers() != null && !user.getLayers().isEmpty()) {
            claims.put("layers", user.getLayers().stream()
                    .map(layer -> layer.getId().toString())
                    .toList());
        }

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    // Валидация токена
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            final String tokenType = extractClaim(token, claims -> claims.get("type", String.class));

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token)
                    && isValidTokenType(tokenType)
                    && isUserConsistent(token, (User) userDetails);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // Извлечение информации из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("user_id", Long.class));
    }

    public Role extractUserRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return Role.valueOf(role);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            log.error("Token expired: {}", ex.getMessage());
            throw new JwtException("Token expired");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT: {}", ex.getMessage());
            throw new JwtException("Unsupported token");
        } catch (MalformedJwtException ex) {
            log.error("Malformed JWT: {}", ex.getMessage());
            throw new JwtException("Invalid token format");
        } catch (SecurityException ex) {
            log.error("Invalid signature: {}", ex.getMessage());
            throw new JwtException("Invalid token signature");
        } catch (IllegalArgumentException ex) {
            log.error("Empty claims: {}", ex.getMessage());
            throw new JwtException("Token claims are empty");
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isValidTokenType(String tokenType) {
        return "access".equals(tokenType) || "refresh".equals(tokenType);
    }

    private boolean isUserConsistent(String token, User user) {
        return extractUserId(token).equals(user.getId())
                && extractUserRole(token).equals(user.getRole());
    }

    private void validateUser(User user) {
        if (user.getRole() == null) {
            throw new IllegalStateException("User role must be defined");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalStateException("Username must be defined");
        }
    }
}