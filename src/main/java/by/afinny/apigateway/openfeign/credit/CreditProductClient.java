package by.afinny.apigateway.openfeign.credit;

import by.afinny.apigateway.dto.credit.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("CREDIT/auth/credit-products")
public interface CreditProductClient {

    @GetMapping
    ResponseEntity<List<ProductDto>> getProducts();
}
