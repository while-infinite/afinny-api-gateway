package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.ActiveDepositDto;
import by.afinny.apigateway.dto.deposit.DepositDto;
import by.afinny.apigateway.dto.deposit.RequestAutoRenewalDto;
import by.afinny.apigateway.dto.deposit.WithdrawDepositDto;
import by.afinny.apigateway.openfeign.deposit.DepositClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static by.afinny.apigateway.util.Utils.getClientId;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/deposits")
@Tag(name = "Deposit Controller", description = "Manage actions with deposits")
public class DepositController {

    public static final String URL_DEPOSITS = "/api/v1/deposits";
    public static final String URL_DEPOSITS_AUTO_RENEWAL = "/{agreementId}/auto-renewal";

    private final DepositClient depositClient;


    @Operation(summary = "Revoke deposit", description = "Early revoke deposit agreement")
    @ApiResponse(responseCode = "200", description = "Deposit revoked")
    @PatchMapping("{agreementId}/revocation")
    public ResponseEntity<Void> earlyWithdrawalDeposit(Authentication authentication,
                                                       @PathVariable UUID agreementId,
                                                       @RequestBody WithdrawDepositDto withdrawDepositDto) {
        return depositClient.earlyWithdrawalDeposit(getClientId(authentication), agreementId, withdrawDepositDto);
    }

    @Operation(summary = "Getting deposit", description = "Get information about deposit")
    @ApiResponse(responseCode = "200", description = "Found the deposit", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = DepositDto.class))})
    @GetMapping("{agreementId}")
    ResponseEntity<DepositDto> getDeposit(Authentication authentication,
                                          @PathVariable UUID agreementId,
                                          @RequestParam UUID cardId) {
        return depositClient.getDeposit(getClientId(authentication), agreementId, cardId);
    }

    @Operation(summary = "Get active deposits", description = "Get information about all the client's active deposits")
    @ApiResponse(responseCode = "200", description = "Deposits were found", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ActiveDepositDto.class))})
    @GetMapping
    public ResponseEntity<List<ActiveDepositDto>> getActiveDeposits(Authentication authentication) {
        return depositClient.getActiveDeposits(getClientId(authentication));
    }

    @Operation(summary = "Update auto renewal", description = "Update auto renewal in agreement")
    @ApiResponse(responseCode = "200", description = "Auto renewal updated")
    @PatchMapping("/{agreementId}/auto-renewal")
    ResponseEntity<Void> updateAutoRenewal(Authentication authentication,
                                           @PathVariable("agreementId") UUID agreementId,
                                           @RequestBody RequestAutoRenewalDto debitAutoRenewalDto) {
        return depositClient.updateAutoRenewal(getClientId(authentication), agreementId, debitAutoRenewalDto);
    }
}
