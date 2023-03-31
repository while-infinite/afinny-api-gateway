package by.afinny.apigateway.controller.investments;

import by.afinny.apigateway.dto.investments.AvailableCurrenciesDto;
import by.afinny.apigateway.dto.investments.AvailableStocksDto;
import by.afinny.apigateway.dto.investments.BrokerageAccountDto;
import by.afinny.apigateway.dto.investments.BrokerageAccountInfoDto;
import by.afinny.apigateway.dto.investments.RequestNewPurchaseDto;
import by.afinny.apigateway.dto.investments.RequestNewSaleDto;
import by.afinny.apigateway.dto.investments.ResponseDealDto;
import by.afinny.apigateway.dto.investments.ResponseNewPurchaseDto;
import by.afinny.apigateway.dto.investments.ResponseNewSaleDto;
import by.afinny.apigateway.openfeign.investments.InvestmentClient;
import by.afinny.apigateway.service.investments.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/investment")
@Tag(name = "Investment Controller", description = "Manage actions with investments")
public class InvestmentController {

    private final InvestmentClient investmentClient;
    private final InvestmentService investmentService;

    @Operation(summary = "Get client current brokerageAccounts", description = "Get information about active brokerageAccounts")
    @ApiResponse(responseCode = "200", description = "Successfully return brokerageAccounts list",
                 content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BrokerageAccountDto.class))})
    @GetMapping
    public ResponseEntity<List<BrokerageAccountDto>> getClientCurrentBrokerageAccounts(Authentication authentication) {
        return investmentClient.getClientBrokerageAccounts(getClientId(authentication));
    }


    @Operation(summary = "Create new purchase ", description = "Create new purchase at the client's request")
    @ApiResponse(responseCode = "200", description = "Purchase created", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseNewPurchaseDto.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    @PostMapping("/new-purchase")
    public ResponseEntity<ResponseNewPurchaseDto> createNewPurchase(@RequestBody RequestNewPurchaseDto dto,
                                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return investmentClient.createNewPurchase(dto, authorization);
    }

    @Operation(summary = "Create new sale ", description = "Create new sale at the client's request")
    @ApiResponse(responseCode = "200", description = "Sale created", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseNewSaleDto.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    @PostMapping("/new-sale")
    public ResponseEntity<ResponseNewSaleDto> createNewSale(@RequestBody RequestNewSaleDto dto,
                                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return investmentClient.createNewSale(dto, authorization);
    }

    @Operation(summary = "view list of currencies and precious metals",
               description = "give user a list with currencies and precious metals information from Moscow Exchange")
    @ApiResponse(responseCode = "200",
                 description = "list of currencies and precious metals",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = AvailableCurrenciesDto.class))})
    @GetMapping("/currency")
    public ResponseEntity<AvailableCurrenciesDto.AllAvailableCurrencies> getAvailableCurrencies() {
        return ResponseEntity.ok().body(investmentService.getAllAvailableCurrencies());
    }

    @Operation(summary = "Getting available stocks from Moscow exchange", description = "View stocks available for purchase")
    @ApiResponse(responseCode = "200",
                 description = "Found stocks",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = AvailableStocksDto.AllAvailableStocks.class))})
    @GetMapping("/address")
    public ResponseEntity<AvailableStocksDto.AllAvailableStocks> getAvailableStocks() {
        return ResponseEntity.ok().body(investmentService.getAllAvailableStocks());
    }

    @Operation(summary = "Getting operation history display", description = "Operation history display")
    @ApiResponse(responseCode = "200",
            description = "Found deal details",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDealDto.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    @GetMapping("/history")
    public ResponseEntity<List<ResponseDealDto>> getDetailsDeals(@RequestParam UUID brokerageAccountId,
                                                                 Authentication authentication,
                                                                 @RequestParam Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize) {
        return investmentClient.getDetailsDeals(brokerageAccountId, getClientId(authentication), pageNumber, pageSize);
    }

    @Operation(summary = "Getting client brokerage account info", description = "Brokerage account info display")
    @ApiResponse(responseCode = "200",
            description = "Found brokerage account info",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BrokerageAccountInfoDto.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    @GetMapping("/brokerageAccountInfo/{brokerageAccountId}")
    public ResponseEntity<BrokerageAccountInfoDto> getClientBrokerageAccount(@PathVariable UUID brokerageAccountId) {
        return investmentClient.getClientBrokerageAccount(brokerageAccountId);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }



}
