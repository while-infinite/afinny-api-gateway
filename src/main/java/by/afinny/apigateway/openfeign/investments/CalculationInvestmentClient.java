package by.afinny.apigateway.openfeign.investments;

import by.afinny.apigateway.dto.investments.ResponseMoneySumDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("INVESTMENTS/auth/calculations")
public interface CalculationInvestmentClient {

    @GetMapping("/investment")
    ResponseEntity<ResponseMoneySumDto> getMoneySum(@RequestParam UUID brokerageAccountId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
}
