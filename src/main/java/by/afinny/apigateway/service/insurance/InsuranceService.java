package by.afinny.apigateway.service.insurance;

import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyFromUser;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyToInsuranceService;
import by.afinny.apigateway.dto.insurance.SuggestionDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface InsuranceService {

    ResponseEntity<SuggestionDto.Suggestions> getAddressParameters(String query);

    ResponseClientDataDto getUserInformation(Authentication authentication);

    RequestNewRealEstatePolicyToInsuranceService getAllFieldsAndSendRequestToService (Authentication authentication, RequestNewRealEstatePolicyFromUser requestNewRealEstatePolicyFromUser);

}
