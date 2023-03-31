package by.afinny.apigateway.openfeign.insurance;

import by.afinny.apigateway.dto.insurance.PolicyInfoDto;
import by.afinny.apigateway.dto.insurance.ResponseApplicationInsuranceTypeDto;
import by.afinny.apigateway.dto.insurance.ResponsePaymentDetailsDto;
import by.afinny.apigateway.dto.insurance.ResponseRejectionLetterDto;
import by.afinny.apigateway.dto.insurance.ResponseUserPolicyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("INSURANCE-SERVICE/auth/insurance")
public interface InsuranceClient {

    @GetMapping
    ResponseEntity<List<ResponseUserPolicyDto>> getUserPolicies(@RequestParam UUID clientId);

    @GetMapping("/types")
    ResponseEntity<List<ResponseApplicationInsuranceTypeDto>> getInsuranceTypes();

    @GetMapping("/{application-id}/report")
    ResponseEntity<ResponseRejectionLetterDto> getRejectionLetter(@RequestParam UUID clientId,
                                                                  @PathVariable("application-id") UUID applicationId);

    @GetMapping("/{applicationId}")
    ResponseEntity<PolicyInfoDto> getPolicyInformation(@RequestParam UUID clientId,
                                                       @PathVariable UUID applicationId);

    @GetMapping("/{application-id}/payment-details")
    ResponseEntity<ResponsePaymentDetailsDto> getPaymentDetails(@RequestParam UUID clientId,
                                                                @PathVariable("application-id") UUID applicationId);

    @DeleteMapping("/{application-id}/revocation")
    ResponseEntity<Void> cancelPolicyApplication(@RequestParam UUID clientId,
                                                 @PathVariable("application-id") UUID applicationId);
}