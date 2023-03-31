package by.afinny.apigateway.openfeign.deposit;

import by.afinny.apigateway.dto.deposit.ClientDto;
import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@FeignClient("DEPOSIT/auth/accounts")
public interface FindByPhoneClient {

    @GetMapping
    ResponseEntity<ClientDto> getClientByPhone(@RequestParam UUID clientId,
                                               @RequestParam(name = "mobilePhone") String mobilePhone,
                                               @RequestParam(name = "currency_code") CurrencyCode currencyCode);
}
