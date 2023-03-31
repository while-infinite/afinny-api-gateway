package by.afinny.apigateway.openfeign.deposit;

import by.afinny.apigateway.dto.deposit.RequestDepositCardOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("DEPOSIT/auth/deposit-card-orders")
public interface CardOrderClient {

    @PostMapping("new")
    ResponseEntity<Void> orderNewCard(@RequestParam UUID clientId, RequestDepositCardOrderDto cardOrderDto);
}