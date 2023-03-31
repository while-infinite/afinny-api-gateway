package by.afinny.apigateway.openfeign.investments;

import by.afinny.apigateway.dto.investments.RequestNewAccountAgreeDto;
import by.afinny.apigateway.dto.investments.ResponseNewAccountAgreeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("INVESTMENTS/auth/investment-order")
public interface OrderClient {
    @PostMapping("/new-account")
    ResponseEntity<ResponseNewAccountAgreeDto> createAccountAgree(RequestNewAccountAgreeDto requestNewAccountAgreeDto);
}
