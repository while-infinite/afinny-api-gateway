package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.JwtDto;
import by.afinny.apigateway.dto.userservice.LoginByPinDto;
import by.afinny.apigateway.dto.userservice.LoginDto;
import by.afinny.apigateway.dto.userservice.SetupPasswordDto;
import by.afinny.apigateway.openfeign.userservice.AuthenticationClient;
import by.afinny.apigateway.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/login")
@Tag(
        name = "Authentication Controller", description = "Manage client's authentication"
)
public class AuthenticationController {

    public static final String URL_LOGIN = "/api/v1/login";
    public static final String URL_PASSWORD = "/password";
    public static final String URL_PIN = "/pin";
    public static final String URL_TOKEN = "/token";

    private final AuthenticationService authenticationService;
    private final AuthenticationClient authenticationClient;

    @Operation(summary = "Authenticate user", description = "Verify password and return tokens")
    @ApiResponse(responseCode = "200", description = "User has been authenticated",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = JwtDto.class))})
    @PostMapping()
    public ResponseEntity<JwtDto> authenticateUser(
            @RequestBody @Parameter(description = "login and password") LoginDto loginDto) {

        JwtDto tokens = authenticationService.login(loginDto);
        return ResponseEntity.ok(tokens);
    }

    @Operation(summary = "Set new password", description = "Reset DB entry's password column")
    @ApiResponse(responseCode = "200", description = "Password has been updated")
    @PatchMapping("password")
    public ResponseEntity<Void> setNewPassword(
            @RequestParam(name = "mobilePhone") @Parameter(description = "mobile phone number") String mobilePhone,
            @RequestBody @Parameter(description = "new password") SetupPasswordDto newPassword) {
        return authenticationClient.setNewPassword(mobilePhone, newPassword.getNewPassword());
    }

    @Operation(summary = "Authenticate user by pin", description = "Verify by pin and return tokens")
    @ApiResponse(responseCode = "200", description = "User has been authenticated",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtDto.class))})
    @PostMapping("pin")
    public ResponseEntity<JwtDto> authenticateUserByPin(@RequestBody LoginByPinDto loginByPinDto) {

        JwtDto jwtDto = authenticationService.loginByPin(loginByPinDto);
        return ResponseEntity.ok(jwtDto);
    }

    @Operation(summary = "Refresh token", description = "Return new generate access token")
    @ApiResponse(responseCode = "200", description = "New token generated",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = JwtDto.class))})
    @GetMapping("token")
    public ResponseEntity<JwtDto> refreshToken(HttpServletRequest request) {

        JwtDto tokens = authenticationService.generateNewToken(request);
        return ResponseEntity.ok(tokens);
    }
}
