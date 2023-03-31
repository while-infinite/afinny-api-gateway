package by.afinny.apigateway.controller.investments;

import by.afinny.apigateway.dto.investments.ChangingPriceAssetDto;
import by.afinny.apigateway.openfeign.investments.InvestmentCalculationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/investment-calculations")
@Tag(name = "InvestmentCalculation Controller", description = "Receiving changes for deals and assets")
public class InvestmentCalculationController {

    private final InvestmentCalculationClient investmentCalculationClient;

    @Operation(summary = "view list with changing prices",
               description = "give user a list with changing prices by it deals")
    @ApiResponse(responseCode = "200", description = "list with changing prices by user deals",
                 content = {@Content(mediaType = "application/json",
                         array = @ArraySchema(schema = @Schema(implementation = ChangingPriceAssetDto.class)))})
    @GetMapping("/investment/exchange-rate")
    public ResponseEntity<List<ChangingPriceAssetDto>> getExchangeRate(
            @RequestParam("brokerageAccountId") UUID brokerageAccountId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        return ResponseEntity.ok().body(investmentCalculationClient.getExchangeRate(brokerageAccountId, authorization).getBody());
    }
}