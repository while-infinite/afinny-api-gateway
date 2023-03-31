package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.RequestDepositCardOrderDto;
import by.afinny.apigateway.openfeign.deposit.CardOrderClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/deposit-card-orders")
@Tag(name = "Cards Orders Controller", description = "Manage cards orders")
public class DepositCardOrderController {

    public static final String URL_CARD_ORDERS = "/api/v1/deposit-card-orders";
    public static final String URL_NEW = "/new";

    private final CardOrderClient cardOrderClient;

    @Operation(summary = "Order new card", description = "Order new deposit card")
    @ApiResponse(responseCode = "200", description = "Card is ordered")
    @PostMapping("new")
    public ResponseEntity<Void> orderNewCard(@RequestBody RequestDepositCardOrderDto cardOrderDto, Authentication authentication) {
        return cardOrderClient.orderNewCard(getClientId(authentication), cardOrderDto);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
