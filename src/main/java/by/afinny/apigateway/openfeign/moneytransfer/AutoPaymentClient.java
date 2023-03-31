package by.afinny.apigateway.openfeign.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.AutoPaymentDto;
import by.afinny.apigateway.dto.moneytransfer.AutoPaymentsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("MONEY-TRANSFER/auth/autopayments")
public interface AutoPaymentClient {

    @PatchMapping
    ResponseEntity<AutoPaymentDto> updateAutoPayment(@RequestParam UUID transferId,
                                                     @RequestBody AutoPaymentDto autoPaymentDto);
    @GetMapping
    ResponseEntity<List<AutoPaymentsDto>> viewAutoPayments(@RequestParam UUID clientId);
}
