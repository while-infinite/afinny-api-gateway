package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.CreditBalanceDto;
import by.afinny.apigateway.dto.credit.CreditDto;
import by.afinny.apigateway.dto.credit.CreditScheduleDto;
import by.afinny.apigateway.dto.credit.DetailsDto;
import by.afinny.apigateway.dto.credit.ResponseOperationDto;
import by.afinny.apigateway.openfeign.credit.CreditClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/credits")
@Tag(name = "Credit Controller", description = "Manage actions with credits")
public class CreditController {

    private final CreditClient creditClient;

    @GetMapping
    @Operation(summary = "Get client current credits", description = "Get information about active credits")
    @ApiResponse(responseCode = "200", description = "Successfully return credits list",
                 content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreditDto.class))})
    public ResponseEntity<List<CreditDto>> getClientCurrentCredits(Authentication authentication) {
        return creditClient.getClientCreditsWithActiveStatus(getClientId(authentication));
    }

    @Operation(summary = "Getting information about credit", description = "Get information about about the client's credit")
    @ApiResponse(responseCode = "200", description = "Successfully found credit and return information about credit",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = CreditBalanceDto.class))})
    @GetMapping("/{creditId}")
    public ResponseEntity<CreditBalanceDto> getCreditBalance(Authentication authentication, @PathVariable UUID creditId) {
        return creditClient.getCreditBalance(getClientId(authentication), creditId);
    }

    @Operation(summary = "Getting information about credit's payment schedule",
            description = "Get information about about the client's credit payment schedule")
    @ApiResponse(responseCode = "200",
            description = "Successfully found credit's payment schedule and return information about payment schedule",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreditScheduleDto.class))})
    @GetMapping("/{creditId}/schedule")
    public ResponseEntity<CreditScheduleDto> getPaymentSchedule(Authentication authentication, @PathVariable UUID creditId) {
        return creditClient.getPaymentSchedule(getClientId(authentication), creditId);
    }
    @Operation(summary = "Getting credit details",
            description = "Get details for credit payment")
    @ApiResponse(responseCode = "200",
            description = "If details for credit payment was successfully received then return status OK",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = DetailsDto.class))})
    @GetMapping("/{agreementId}/details")
    public ResponseEntity<DetailsDto> getDetailsForPayment(Authentication authentication, @PathVariable UUID agreementId) {
        return creditClient.getDetailsForPayment(getClientId(authentication), agreementId);
    }

    @Operation(summary = "Getting history payment of credit", description = "Get information about operation on credit")
    @ApiResponse(responseCode = "200",
            description = "Response operation on credit",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    @GetMapping("/{creditId}/history")
    public ResponseEntity<List<ResponseOperationDto>> getDetailsOfLastPayments(@RequestParam UUID clientId,
                                                                               @PathVariable UUID creditId,
                                                                               @RequestParam Integer pageNumber,
                                                                               @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize) {
        return creditClient.getDetailsOfLastPayments(clientId, creditId, pageNumber, pageSize);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
