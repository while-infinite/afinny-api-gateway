package by.afinny.apigateway.openfeign.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("https://iss.moex.com/iss/engines")
public interface MoscowExchangeAPI {

    @GetMapping("/currency/markets/selt/securities.json")
    String getAvailableCurrencies(@RequestParam("iss.meta") String issMeta,
                                  @RequestParam("iss.only") String issOnly,
                                  @RequestParam("marketdata.columns") String marketDataColumns,
                                  @RequestParam("iss.json") String issJson);

    @GetMapping("/stock/markets/shares/boards/TQBR/securities.json")
    String getAvailableStocks(@RequestParam("iss.meta") String issMeta,
                              @RequestParam("iss.only") String issOnly,
                              @RequestParam("marketdata.columns") String marketDataColumns,
                              @RequestParam("iss.json") String issJson);

}

