package by.afinny.apigateway.openfeign.insurance;

import by.afinny.apigateway.dto.insurance.RequestMedicinePolicyDto;
import by.afinny.apigateway.dto.insurance.RequestNewPolicy;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyToInsuranceService;
import by.afinny.apigateway.dto.insurance.RequestTravelPolicyDto;
import by.afinny.apigateway.dto.insurance.ResponseTravelPolicyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("INSURANCE-SERVICE/auth/insurance-order")
public interface InsuranceOrderClient {

    @PostMapping("/new-cars")
    ResponseEntity<RequestNewPolicy> createNewPolicy(@RequestBody RequestNewPolicy requestNewPolicy);

    @PostMapping("/new-medicine")
    ResponseEntity<RequestMedicinePolicyDto> registerNewMedicinePolicy(@RequestBody RequestMedicinePolicyDto requestMedicinePolicyDto);

    @PostMapping("/new-property")
    ResponseEntity<Void> createNewRealEstatePolicy (@RequestBody RequestNewRealEstatePolicyToInsuranceService requestNewRealEstatePolicyToInsuranceService);

    @PostMapping("/new-travel-program")
    ResponseEntity<ResponseTravelPolicyDto> createNewTravelPolicy(@RequestBody RequestTravelPolicyDto requestTravelPolicyDto);
}
