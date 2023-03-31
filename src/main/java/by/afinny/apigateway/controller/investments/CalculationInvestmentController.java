package by.afinny.apigateway.controller.investments;

import by.afinny.apigateway.dto.investments.ResponseMoneySumDto;
import by.afinny.apigateway.openfeign.investments.CalculationInvestmentClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/calculations")
@RequiredArgsConstructor
public class CalculationInvestmentController {

    private final CalculationInvestmentClient investmentsClient;

    @Operation(summary = "Getting sum of cost stocks, metals and currency", description = "View sum of cost stocks, metals and currency")
    @ApiResponse(responseCode = "200",
            description = "Found sum of cost stocks, metals and currency",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseMoneySumDto.class))})
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    @GetMapping("investment")
    public ResponseEntity<ResponseMoneySumDto> getMoneySum(@RequestParam UUID brokerageAccountId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return investmentsClient.getMoneySum(brokerageAccountId, authorization);
    }
}