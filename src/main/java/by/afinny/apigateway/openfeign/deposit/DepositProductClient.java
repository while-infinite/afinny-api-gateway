package by.afinny.apigateway.openfeign.deposit;

import by.afinny.apigateway.dto.deposit.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("DEPOSIT/auth/deposit-products")
public interface DepositProductClient {

    @GetMapping
    ResponseEntity<List<ProductDto>> getActiveDepositProducts();
}