package by.afinny.apigateway.openfeign.deposit;

import by.afinny.apigateway.dto.deposit.CardProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("DEPOSIT/auth/cards-products")
public interface CardProductsClient {

    @GetMapping()
    ResponseEntity<List<CardProductDto>> getAllCardProducts();
}
