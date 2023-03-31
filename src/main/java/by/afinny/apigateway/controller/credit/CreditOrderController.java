package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.RequestCreditOrderDto;
import by.afinny.apigateway.dto.credit.ResponseCreditOrderDto;
import by.afinny.apigateway.openfeign.credit.CreditOrderClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/credit-orders")
@Tag(name = "Credit Order Controller", description = "Manage actions with credit orders")
public class CreditOrderController {

    public static final String URL_CREDIT_ORDERS = "/api/v1/credit-orders/";

    public static final String PARAM_CREDIT_ORDER_ID = "/{creditOrderId}/pending";

    private final CreditOrderClient creditOrderClient;

    @Operation(summary = "Create credit order", description = "Create a credit order at the client's request")
    @ApiResponse(responseCode = "200", description = "Order created", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseCreditOrderDto.class))})
    @PostMapping("new")
    public ResponseEntity<ResponseCreditOrderDto> createOrder(@RequestBody RequestCreditOrderDto dto,
                                                              Authentication authentication) {
        return creditOrderClient.createOrder(getClientId(authentication), dto);
    }

    @Operation(summary = "Get credit orders", description = "Get information about client's credit orders")
    @ApiResponse(responseCode = "200", description = "Found the credit orders", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseCreditOrderDto.class))})
    @GetMapping
    public ResponseEntity<List<ResponseCreditOrderDto>> getCreditOrders(Authentication authentication) {
        return creditOrderClient.getCreditOrders(getClientId(authentication));
    }

    @Operation(summary = "Delete credit order", description = "Recall and delete credit order")
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("{creditOrderId}/pending")
    public ResponseEntity<Void> deleteCreditOrder(Authentication authentication, @PathVariable UUID creditOrderId) {
        return creditOrderClient.deleteCreditOrder(getClientId(authentication), creditOrderId);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
