package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.FingerprintDto;
import by.afinny.apigateway.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/fingerprint")
@Tag(name = "Fingerprint Controller", description = "Manage work with fingerprint")
public class FingerprintController {

    public static final String URL_FINGERPRINT = "/api/v1/fingerprint";

    private final AuthenticationService authenticationService;

    @Operation(summary = "Create fingerprint", description = "Create new fingerprint")
    @ApiResponse(responseCode = "200", description = "Fingerprint created")
    @PostMapping()
    public ResponseEntity<Void> createFingerprint(@RequestBody FingerprintDto fingerprintDto, Authentication authentication) {
        return authenticationService.createFingerprint(fingerprintDto, getClientId(authentication));
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
