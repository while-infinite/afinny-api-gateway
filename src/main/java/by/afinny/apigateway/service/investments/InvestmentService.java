package by.afinny.apigateway.service.investments;

import by.afinny.apigateway.dto.investments.AvailableCurrenciesDto;
import by.afinny.apigateway.dto.investments.AvailableStocksDto;

public interface InvestmentService {

    AvailableCurrenciesDto.AllAvailableCurrencies getAllAvailableCurrencies();

    AvailableStocksDto.AllAvailableStocks getAllAvailableStocks();


}
