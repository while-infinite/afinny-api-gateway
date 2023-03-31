package by.afinny.apigateway.controller.infoservice;

import by.afinny.apigateway.dto.infoservice.CurrencyExchangeRateDto;
import by.afinny.apigateway.dto.infoservice.ResponseExchangeRateDto;
import by.afinny.apigateway.mapper.infoservice.ResponseExchangeRateDtoMapper;
import by.afinny.apigateway.openfeign.infoservice.ExchangeRateClient;
import by.afinny.apigateway.service.investments.InvestmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Tag(name = "Exchange Rate Controller", description = "Getting data about exchange rates")
public class ExchangeRateController {

    private final ExchangeRateClient exchangeRateClient;
    private final InvestmentService investmentService;
    private final ResponseExchangeRateDtoMapper responseExchangeRateDtoMapper;

    @Operation(summary = "Getting actual exchange rate", description = "Get information about exchange rate")
    @ApiResponse(responseCode = "200",
            description = "Found the exchange rates",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ResponseExchangeRateDto.class)))})
    @GetMapping("exchange-rates")
    public ResponseEntity<ResponseExchangeRateDto.ResponseExchangeRateList> getActualExchangeRates() {
        return ResponseEntity.ok().body(
                responseExchangeRateDtoMapper.toResponseExchangeRateDtoList(investmentService.getAllAvailableCurrencies())
        );
    }

    @Operation(summary = "Getting actual currency exchange rate",
               description = "Get information about currency exchange rate")
    @ApiResponse(responseCode = "200",
                 description = "Found the currency exchange rates",
                 content = {@Content(mediaType = "application/json",
                         array = @ArraySchema(schema = @Schema(implementation = CurrencyExchangeRateDto.class)))})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("rates")
    public ResponseEntity<CurrencyExchangeRateDto> getCurrencyExchangeRate(@RequestParam String baseCurrency,
                                                                           @RequestParam String currency) {
        return exchangeRateClient.getCurrencyExchangeRate(baseCurrency, currency);
    }
}
