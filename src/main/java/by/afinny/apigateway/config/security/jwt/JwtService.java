package by.afinny.apigateway.config.security.jwt;

import by.afinny.apigateway.util.constant.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtService {

    public String generateAccessToken(String uuid) {
        SecretKey key = Keys.hmacShaKeyFor(JWT.KEY.getValue().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claim(JWT.UUID.getValue(), uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis() + Integer.parseInt(JWT.ACCESS_TOKEN_EXPIRATION.getValue()))))
                .signWith(key).compact();
    }

    public String generateRefreshToken(String uuid) {
        SecretKey key = Keys.hmacShaKeyFor(JWT.KEY.getValue().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claim(JWT.UUID.getValue(), uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis() + Integer.parseInt(JWT.REFRESH_TOKEN_EXPIRATION.getValue()))))
                .signWith(key).compact();
    }
}
