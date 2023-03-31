package by.afinny.apigateway.mapper.infoservice;

import by.afinny.apigateway.dto.infoservice.ResponseExchangeRateDto;
import by.afinny.apigateway.dto.infoservice.constant.CurrencyCode;
import by.afinny.apigateway.dto.investments.AvailableCurrenciesDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseExchangeRateDtoMapper {
    private final List<String> CURRENCY_CODES = List.of("JPYRUB", "USDRUB", "EURRUB", "CNYRUB");

    public ResponseExchangeRateDto.ResponseExchangeRateList toResponseExchangeRateDtoList(AvailableCurrenciesDto.AllAvailableCurrencies currencies) {
        if (currencies == null) {
            return null;
        }

        List<ResponseExchangeRateDto> list = currencies.getCurrencies().stream()
                .filter(currency -> isCorrespondCurrency(currency.getSecid()))
                .map(this::toResponseExchangeRateDto)
                .collect(Collectors.toList());

        ResponseExchangeRateDto.ResponseExchangeRateList responseList = new ResponseExchangeRateDto.ResponseExchangeRateList();
        responseList.setExchangeRates(list);
        return responseList;
    }

    public ResponseExchangeRateDto toResponseExchangeRateDto(AvailableCurrenciesDto currency) {
        if (currency == null) {
            return null;
        }
        String secid = currency.getSecid();
        String currencyCode = null;
        String name = null;

        if(secid.contains(CurrencyCode.JPY.name())){
            currencyCode = "JPY/RUB";
            name = "Japanese Yen";
        }
        if(secid.contains(CurrencyCode.USD.name())){
            currencyCode = "USD/RUB";
            name = "US Dollar";
        }
        if(secid.contains(CurrencyCode.EUR.name())){
            currencyCode = "EUR/RUB";
            name = "Euro";
        }
        if(secid.contains(CurrencyCode.CNY.name())){
            currencyCode = "CHY/RUB";
            name = "Chinese Yuan";
        }

        return ResponseExchangeRateDto.builder()
                .secid(currency.getSecid())
                .boardid(currency.getBoardid())
                .buyingRate(currency.getLast() * 1.05)
                .sellingRate(currency.getLast() * 0.95)
                .currencyCode(currencyCode)
                .name(name)
                .build();
    }


    private boolean isCorrespondCurrency(String secid) {
        for (String currencyCode : CURRENCY_CODES) {
            if (secid.contains(currencyCode))
                return true;
        }
        return false;
    }


}