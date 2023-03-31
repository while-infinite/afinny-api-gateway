package by.afinny.apigateway.service.investments.impl;

import by.afinny.apigateway.dto.investments.AvailableCurrenciesDto;
import by.afinny.apigateway.dto.investments.AvailableStocksDto;
import by.afinny.apigateway.mapper.investments.AvailableCurrenciesMapper;
import by.afinny.apigateway.mapper.investmets.AvailableStocksMapper;
import by.afinny.apigateway.openfeign.api.MoscowExchangeAPI;
import by.afinny.apigateway.service.investments.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static by.afinny.apigateway.service.investments.constant.ParamForMoscowExchangeCurrencies.ISS_META;
import static by.afinny.apigateway.service.investments.constant.ParamForMoscowExchangeCurrencies.MARKET_DATA_COLUMNS;
import static by.afinny.apigateway.service.investments.constant.ParamForMoscowExchangeStocks.ISS_JSON;
import static by.afinny.apigateway.service.investments.constant.ParamForMoscowExchangeStocks.ISS_ONLY;
import static by.afinny.apigateway.service.investments.constant.ParamForMoscowExchangeStocks.MARKET_DATA_COLUMNS_STOCKS;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {

    private final MoscowExchangeAPI moscowExchangeAPI;
    private final AvailableCurrenciesMapper availableCurrenciesMapper;
    private final AvailableStocksMapper availableStocksMapper;

    @Cacheable("availableCurrenciesCache")
    @Override
    public AvailableCurrenciesDto.AllAvailableCurrencies getAllAvailableCurrencies() {
        String response = Optional.of(moscowExchangeAPI.getAvailableCurrencies(
                ISS_META.getParam(),
                ISS_ONLY.getParam(),
                MARKET_DATA_COLUMNS.getParam(),
                ISS_JSON.getParam())).orElseThrow(() ->
                new RuntimeException("Data from Moscow Exchange is not available"));
        return availableCurrenciesMapper.toCurrenciesDtoList(response);
    }

    @Override
    public AvailableStocksDto.AllAvailableStocks getAllAvailableStocks() {
        String moscowAvailableStocks = Optional.of(moscowExchangeAPI.getAvailableStocks(ISS_META.getParam(),
                ISS_ONLY.getParam(),
                MARKET_DATA_COLUMNS_STOCKS.getParam(),
                ISS_JSON.getParam())).orElseThrow(() ->
                new RuntimeException("Data from Moscow Exchange is not available"));
        return availableStocksMapper.toAllAvailableStocks(moscowAvailableStocks);
    }

}
