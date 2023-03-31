package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.ClientDto;
import by.afinny.apigateway.dto.userservice.PassportDto;
import by.afinny.apigateway.dto.userservice.RegisteredUserDto;
import by.afinny.apigateway.dto.userservice.RequestClientDto;
import by.afinny.apigateway.dto.userservice.RequestRegisteringUserDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDto;
import by.afinny.apigateway.dto.userservice.UserDto;
import by.afinny.apigateway.exception.dto.AccountExistErrorDto;
import by.afinny.apigateway.exception.dto.ErrorDto;
import by.afinny.apigateway.mapper.ClientDtoMapper;
import by.afinny.apigateway.openfeign.userservice.RegistrationClient;
import by.afinny.apigateway.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/registration")
@Tag(
        name = "Registration Controller", description = "Register client in the application"
)
public class RegistrationController {

    private final RegistrationClient registrationClient;
    private final ClientDtoMapper clientDtoMapper;
    private final ClientService clientService;

    @Operation(summary = "Process Client data (mobile phone)", description = "Check if mobile phone has been registered in the Bank System")
    @ApiResponse(responseCode = "200", description = "User with this phone number hasn't registered",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))})
    @ApiResponse(responseCode = "409", description = "User with this phone number has been already registered",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountExistErrorDto.class))})
    @GetMapping
    public ResponseEntity<UserDto> verifyMobilePhone(
            @RequestParam(name = "mobilePhone")
            @Parameter(description = "mobile phone number") String mobilePhone) {

        ClientDto client = registrationClient.verifyMobilePhone(mobilePhone).getBody();
        return ResponseEntity.ok(castFromClientToUser(client));
    }

    @Operation(summary = "Register client", description = "Save client into application's profiles DB")
    @ApiResponse(responseCode = "200", description = "Client has been successfully registered",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisteredUserDto.class))})
    @ApiResponse(responseCode = "400", description = "Client wasn't found",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    @PatchMapping("user-profile")
    public ResponseEntity<Void> registerExistingClient(
            Authentication authentication,
            @RequestBody @Parameter(description = "client data") RequestRegisteringUserDto requestRegisteringUserDto) {
        return registrationClient.registerExistingClient(
                clientService.registerExistingClient(getIdFromAuthentication(authentication), requestRegisteringUserDto)
        );
    }

    @Operation(summary = "Register non-existent client", description = "Save client into application's profiles DB")
    @ApiResponse(responseCode = "200", description = "User has been successfully registered",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseClientDto.class))})
    @PostMapping("user-profile/new")
    public ResponseEntity<Void> registerNonClient(
            @RequestBody @Parameter(description = "client data") RequestClientDto requestClientDto) {
        return registrationClient.registerNonClient(requestClientDto);
    }

    @Operation(summary = "Process Client data (passport number)", description = "Verify is passport number already exist")
    @ApiResponse(responseCode = "200", description = "Passport number was not found",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisteredUserDto.class))})
    @ApiResponse(responseCode = "400", description = "Passport number exist",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})
    @PostMapping("user-profile/verification")
    public ResponseEntity<Void> verifyPassportNumber(
            @RequestBody @Parameter(description = "Passport Number") PassportDto passportNumber) {
        return registrationClient.verifyPassportNumber(passportNumber);
    }

    private UserDto castFromClientToUser(ClientDto client) {
        return client.getId() == null ? clientDtoMapper.clientToUser(client) : client;
    }

    private UUID getIdFromAuthentication(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
