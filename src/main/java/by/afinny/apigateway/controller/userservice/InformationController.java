package by.afinny.apigateway.controller.userservice;


import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.openfeign.userservice.InformationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/information")
@Tag(
        name = "Information Controller", description = "Getting customer data"
)
public class InformationController {

    public static final String INFORMATION_URL = "/api/v1/information";
    public static final String CLIENT_ID_PARAMETER = "clientId";

    private final InformationClient informationClient;

    @Operation(summary = "Getting information about a user", description = "Getting information about a user by id")
    @ApiResponse(responseCode = "200", description = "Client has been successfully found",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseClientDataDto.class))})
    @GetMapping()
    public ResponseEntity<ResponseClientDataDto> getClientData(Authentication authentication) {

        return informationClient.getClientData(getClientId(authentication));
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
