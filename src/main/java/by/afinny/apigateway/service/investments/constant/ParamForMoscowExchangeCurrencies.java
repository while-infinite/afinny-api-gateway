package by.afinny.apigateway.service.investments.constant;

import lombok.Getter;

public enum ParamForMoscowExchangeCurrencies {
    ISS_META("off"),
    ISS_ONLY("marketdata"),
    MARKET_DATA_COLUMNS("SECID,BOARDID,LAST,CHANGE,LASTTOPREVPRICE"),
    ISS_JSON("extended");

    ParamForMoscowExchangeCurrencies(String param) {
        this.param = param;
    }

    @Getter
    private final String param;
}
