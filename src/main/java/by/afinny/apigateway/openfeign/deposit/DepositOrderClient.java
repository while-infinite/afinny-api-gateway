package by.afinny.apigateway.openfeign.deposit;

import by.afinny.apigateway.dto.deposit.RequestNewDepositDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("DEPOSIT/auth/deposit-orders")
public interface DepositOrderClient {

    @PostMapping("new")
    ResponseEntity<Void> createNewDeposit(@RequestParam UUID clientId,
                                          @RequestBody RequestNewDepositDto requestNewDepositDto);
}
