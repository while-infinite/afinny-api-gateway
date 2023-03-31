package by.afinny.apigateway.openfeign.infoservice;

import by.afinny.apigateway.dto.infoservice.CurrencyExchangeRateDto;
import by.afinny.apigateway.dto.infoservice.ResponseExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("INFO-SERVICE")
public interface ExchangeRateClient {

    @GetMapping("exchange-rates")
    ResponseEntity<List<ResponseExchangeRateDto>> getActualExchangeRates();

    @GetMapping("auth/rates")
    ResponseEntity<CurrencyExchangeRateDto> getCurrencyExchangeRate(@RequestParam String baseCurrency,
                                                                    @RequestParam String currency);
}