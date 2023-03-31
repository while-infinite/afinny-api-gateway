package by.afinny.apigateway.openfeign.investments;

import by.afinny.apigateway.dto.investments.BrokerageAccountDto;
import by.afinny.apigateway.dto.investments.BrokerageAccountInfoDto;
import by.afinny.apigateway.dto.investments.RequestNewPurchaseDto;
import by.afinny.apigateway.dto.investments.RequestNewSaleDto;
import by.afinny.apigateway.dto.investments.ResponseDealDto;
import by.afinny.apigateway.dto.investments.ResponseNewPurchaseDto;
import by.afinny.apigateway.dto.investments.ResponseNewSaleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("INVESTMENTS/auth/investment")
public interface InvestmentClient {

    @GetMapping
    ResponseEntity<List<BrokerageAccountDto>> getClientBrokerageAccounts(@RequestParam UUID clientId);

    @GetMapping("/history")
    ResponseEntity<List<ResponseDealDto>> getDetailsDeals(@RequestParam UUID brokerageAccountId,
                                                          @RequestParam UUID clientId,
                                                          @RequestParam Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize);
    @PostMapping("/new-purchase")
    ResponseEntity<ResponseNewPurchaseDto> createNewPurchase(RequestNewPurchaseDto dto, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
    @PostMapping("/new-sale")
    ResponseEntity<ResponseNewSaleDto> createNewSale(RequestNewSaleDto dto, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);


    @GetMapping("/brokerageAccountInfo/{brokerageAccountId}")
    ResponseEntity<BrokerageAccountInfoDto> getClientBrokerageAccount(@PathVariable UUID brokerageAccountId);

}
