package by.afinny.apigateway.service;

import by.afinny.apigateway.config.security.jwt.JwtService;
import by.afinny.apigateway.dto.userservice.FingerprintDto;
import by.afinny.apigateway.dto.userservice.JwtDto;
import by.afinny.apigateway.dto.userservice.LoginByPinDto;
import by.afinny.apigateway.dto.userservice.LoginDto;
import by.afinny.apigateway.dto.userservice.RequestFingerprintDto;
import by.afinny.apigateway.dto.userservice.RequestLoginByPinDto;
import by.afinny.apigateway.exception.InvalidJwtException;
import by.afinny.apigateway.openfeign.userservice.AuthenticationClient;
import by.afinny.apigateway.openfeign.userservice.FingerprintClient;
import by.afinny.apigateway.util.constant.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationClient authenticationClient;
    private final FingerprintClient fingerprintClient;

    public JwtDto login(LoginDto credentials) {
        UUID clientId = authenticationClient.authenticateUser(credentials).getBody();

        return generateTokens(clientId.toString());
    }

    public JwtDto generateNewToken(HttpServletRequest request) {
        log.info("generateNewToken() method invoked");
        String jwt = request.getHeader(JWT.HEADER.getValue());

        if (jwt == null) {
            throw new RuntimeException("Refresh token is missing");
        }

        String clientId = getClientIdFromJwt(jwt);
        JwtDto tokens = new JwtDto();
        tokens.setAccessToken(jwtService.generateAccessToken(clientId));
        tokens.setRefreshToken(jwt);
        return tokens;
    }

    public JwtDto loginByPin(LoginByPinDto loginByPinDto) {
        log.info("loginByPin() method invoked");
        String clientIdFromJwt = getClientIdFromJwt(loginByPinDto.getToken());
        UUID clientId = authenticationClient
                .authenticateUserByPin(generateRequestForLoginByPin(clientIdFromJwt, loginByPinDto.getFingerprint())).getBody();
        return generateTokens(clientId.toString());
    }

    public ResponseEntity<Void> createFingerprint(FingerprintDto fingerprintDto, UUID clientId) {
        log.info("createFingerprint() method invoked");
        RequestFingerprintDto requestFingerprintDto = generateRequestForCreateFingerprint(clientId, fingerprintDto.getFingerprint());
        return fingerprintClient.createFingerprint(requestFingerprintDto);
    }

    private JwtDto generateTokens(String clientId) {
        JwtDto tokens = new JwtDto();
        tokens.setAccessToken(jwtService.generateAccessToken(clientId));
        tokens.setRefreshToken(jwtService.generateRefreshToken(clientId));
        return tokens;
    }

    private RequestLoginByPinDto generateRequestForLoginByPin(String clientId, String fingerprint) {
        return RequestLoginByPinDto.builder()
                .clientId(UUID.fromString(clientId))
                .fingerprint(fingerprint)
                .build();
    }

    private RequestFingerprintDto generateRequestForCreateFingerprint(UUID clientId, String fingerprint) {
        return RequestFingerprintDto.builder()
                .clientId(clientId)
                .fingerprint(fingerprint)
                .build();
    }

    private String getClientIdFromJwt(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(JWT.KEY.getValue().getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            return String.valueOf(claims.get(JWT.UUID.getValue()));
        } catch (IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidJwtException();
        }
    }
}
