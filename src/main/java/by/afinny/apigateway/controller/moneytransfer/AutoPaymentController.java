package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.AutoPaymentDto;
import by.afinny.apigateway.dto.moneytransfer.AutoPaymentsDto;
import by.afinny.apigateway.openfeign.moneytransfer.AutoPaymentClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/autopayments")
@RequiredArgsConstructor
@Tag(name = "Auto payment Controller", description = "Working on Auto payments")
public class AutoPaymentController {

    public static final String URL_AUTO_PAYMENT = "/api/v1/autopayments";

    private final AutoPaymentClient autoPaymentClient;

    @Operation(summary = "Enable/disable auto payment", description = "allow user to enable/disable auto payment")
    @ApiResponse(responseCode = "200",
                 description = "auto payment status updated",
                 content = {@Content(mediaType = "application/json",
                         array = @ArraySchema(schema = @Schema(implementation = AutoPaymentDto.class)))})
    @PatchMapping
    ResponseEntity<AutoPaymentDto> updateAutoPayment(@RequestParam UUID transferId,
                                                     @RequestBody AutoPaymentDto autoPaymentDto) {
        return autoPaymentClient.updateAutoPayment(transferId, autoPaymentDto);
    }

    @Operation(summary = "view auto payments", description = "allow user to view enabled auto payments")
    @ApiResponse(responseCode = "200",
                 description = "list of auto payments",
                 content = {@Content(mediaType = "application/json",
                         array = @ArraySchema(schema = @Schema(implementation = AutoPaymentsDto.class)))})
    @GetMapping
    public ResponseEntity<List<AutoPaymentsDto>> viewAutoPayments(Authentication authentication) {
        return autoPaymentClient.viewAutoPayments(getClientId(authentication));
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
