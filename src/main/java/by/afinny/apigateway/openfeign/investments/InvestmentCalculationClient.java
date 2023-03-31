package by.afinny.apigateway.openfeign.investments;

import by.afinny.apigateway.dto.investments.ChangingPriceAssetDto;
import by.afinny.apigateway.dto.investments.DealExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("INVESTMENTS/auth/investment-calculations")
public interface InvestmentCalculationClient {

    @GetMapping("/investment")
    ResponseEntity<List<DealExchangeRateDto>> getDealsForCalculation(
            @RequestParam("brokerageAccountId") UUID brokerageAccountId);

    @GetMapping("/investment/exchange-rate")
    ResponseEntity<List<ChangingPriceAssetDto>> getExchangeRate(
            @RequestParam("brokerageAccountId") UUID brokerageAccountId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
}
