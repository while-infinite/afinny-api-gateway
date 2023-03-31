package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.RequestNewDepositDto;
import by.afinny.apigateway.openfeign.deposit.DepositOrderClient;
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

import static by.afinny.apigateway.util.Utils.getClientId;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/deposit-orders")
@Tag(name = "Deposit Order Controller", description = "Manage actions with deposit orders")
public class DepositOrderController {

    private final DepositOrderClient depositOrderClient;

    public static final String DEPOSIT_ORDER_URL = "/api/v1/deposit-orders";
    public static final String NEW_DEPOSIT_URL = "/new";

    @Operation(summary = "Create new deposit", description = "Create new deposit agreement")
    @ApiResponse(responseCode = "200", description = "Deposit created")
    @PostMapping("new")
    ResponseEntity<Void> createNewDeposit(Authentication authentication, @RequestBody RequestNewDepositDto requestNewDepositDto) {
        return depositOrderClient.createNewDeposit(getClientId(authentication), requestNewDepositDto);
    }
}
