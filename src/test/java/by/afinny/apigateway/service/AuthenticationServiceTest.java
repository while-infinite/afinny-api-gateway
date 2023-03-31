package by.afinny.apigateway.service;

import by.afinny.apigateway.config.security.jwt.JwtService;
import by.afinny.apigateway.dto.userservice.JwtDto;
import by.afinny.apigateway.dto.userservice.LoginDto;
import by.afinny.apigateway.dto.userservice.constant.AuthenticationType;
import by.afinny.apigateway.openfeign.userservice.AuthenticationClient;
import by.afinny.apigateway.util.constant.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationClient authenticationClient;
    @Mock
    private JwtService jwtService;
    @Mock
    private HttpServletRequest request;

    private LoginDto loginDto;
    private JwtDto jwtDto;
    private String accessToken;
    private String refreshToken;
    private final UUID CLIENT_ID = UUID.randomUUID();
    private final SecretKey KEY = Keys.hmacShaKeyFor(JWT.KEY.getValue().getBytes(StandardCharsets.UTF_8));

    @BeforeEach
    void setUp() {
        accessToken = Jwts.builder()
                .claim(JWT.UUID.getValue(), CLIENT_ID)
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis()
                        + Integer.parseInt(JWT.ACCESS_TOKEN_EXPIRATION.getValue()))))
                .signWith(KEY).compact();

        refreshToken = Jwts.builder()
                .claim(JWT.UUID.getValue(), CLIENT_ID)
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis()
                        + Integer.parseInt(JWT.REFRESH_TOKEN_EXPIRATION.getValue()))))
                .signWith(KEY).compact();

        loginDto = new LoginDto();
        loginDto.setLogin("login");
        loginDto.setPassword("password");
        loginDto.setType(AuthenticationType.PASSPORT_NUMBER);

        jwtDto = new JwtDto();
        jwtDto.setAccessToken(accessToken);
        jwtDto.setRefreshToken(refreshToken);
    }

    @Test
    @DisplayName("If token is successfully received return the tokens")
    void login_shouldReturnTokens() {
        //ARRANGE
        ResponseEntity<UUID> responseEntity = ResponseEntity.ok(CLIENT_ID);

        when(authenticationClient.authenticateUser(loginDto))
                .thenReturn(responseEntity);
        when(jwtService.generateAccessToken(any(String.class)))
                .thenReturn(accessToken);
        when(jwtService.generateRefreshToken(any(String.class)))
                .thenReturn(refreshToken);
        //ACT
        JwtDto responseJwtDto = authenticationService.login(loginDto);
        //VERIFY
        verifyTokens(responseJwtDto);
    }

    @Test
    @DisplayName("If new token is successfully received return the tokens")
    void generateNewToken_shouldReturnTokens() {
        //ARRANGE
        when(request.getHeader(any(String.class)))
                .thenReturn(refreshToken);
        when(jwtService.generateAccessToken(any(String.class)))
                .thenReturn(accessToken);
        //ACT
        JwtDto responseJwtDto = authenticationService.generateNewToken(request);
        //VERIFY
        verifyTokens(responseJwtDto);
    }

    @Test
    @DisplayName("If authorization header is null then throw Runtime Exception")
    void generateNewToken_ifJwtEqualsNull_thenThrowException() {
        //ARRANGE
        when(request.getHeader(any(String.class)))
                .thenReturn(null);
        //ACT
        ThrowingCallable exception = () -> authenticationService.generateNewToken(request);
        //VERIFY
        assertThatThrownBy(exception).isInstanceOf(RuntimeException.class);
    }

    private void verifyTokens(JwtDto responseJwtDto) {
        assertThat(responseJwtDto.getAccessToken()).isEqualTo(jwtDto.getAccessToken());
        assertThat(responseJwtDto.getRefreshToken()).isEqualTo(jwtDto.getRefreshToken());
    }
}