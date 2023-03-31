package by.afinny.apigateway.openfeign.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CommissionDto;
import by.afinny.apigateway.dto.moneytransfer.constant.CurrencyCode;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("MONEY-TRANSFER/auth/commission")
public interface CommissionClient {

    @GetMapping
    ResponseEntity<CommissionDto> getCommissionData(@RequestParam TransferTypeName typeName,
                                                     @RequestParam CurrencyCode currencyCode);
}

