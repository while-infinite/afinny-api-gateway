package by.afinny.apigateway.service.investments.constant;

import lombok.Getter;

public enum ParamForMoscowExchangeStocks {
    ISS_META("off"),
    ISS_ONLY("marketdata"),
    MARKET_DATA_COLUMNS_STOCKS("SECID,BOARDID,BID,OFFER,LAST,CHANGE,LASTTOPREVPRICE"),
    ISS_JSON("extended");

    ParamForMoscowExchangeStocks(String param) {
        this.param = param;
    }

    @Getter
    private final String param;
}
