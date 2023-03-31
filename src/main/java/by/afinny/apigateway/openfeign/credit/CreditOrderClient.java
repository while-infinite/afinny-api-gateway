package by.afinny.apigateway.openfeign.credit;

import by.afinny.apigateway.dto.credit.RequestCreditOrderDto;
import by.afinny.apigateway.dto.credit.ResponseCreditOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("CREDIT/auth/credit-orders")
public interface CreditOrderClient {

    @PostMapping("new")
    ResponseEntity<ResponseCreditOrderDto> createOrder(@RequestParam UUID clientId, RequestCreditOrderDto dto);

    @GetMapping
    ResponseEntity<List<ResponseCreditOrderDto>> getCreditOrders(@RequestParam UUID clientId);

    @DeleteMapping("{creditOrderId}/pending")
    ResponseEntity<Void> deleteCreditOrder(@RequestParam UUID clientId, @PathVariable UUID creditOrderId);
}
