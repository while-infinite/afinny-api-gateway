package by.afinny.apigateway.service.insurance.impl;

import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyFromUser;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyToInsuranceService;
import by.afinny.apigateway.dto.insurance.SuggestionDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.mapper.InsuranceDtoMapper;
import by.afinny.apigateway.mapper.insurance.SuggestionMapper;
import by.afinny.apigateway.openfeign.insurance.DaDataApiClient;
import by.afinny.apigateway.openfeign.userservice.InformationClient;
import by.afinny.apigateway.service.insurance.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private static final String TOKEN = "Token ffdeec1218f0ce59f1e3bf2ff9ea2a725aa5473e";
    private static final String TYPE = "application/json";

    private final DaDataApiClient daDataApiClient;
    private final SuggestionMapper suggestionMapper;
    private final InformationClient informationClient;
    private final InsuranceDtoMapper insuranceDtoMapper;

    @Override
    public ResponseEntity<SuggestionDto.Suggestions> getAddressParameters(String query) {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Type", TYPE);
        httpHeaders.add("Authorization", TOKEN);

        String jsonQuery = "{ \"query\": " +'\u0022'+ query +'\u0022' + "}";

        String response = daDataApiClient.getAddressParameters(httpHeaders, jsonQuery);

        return ResponseEntity.ok().body(suggestionMapper.toSuggestionDtoList(response));
    }

    @Override
    public ResponseClientDataDto getUserInformation(Authentication authentication) {
        return informationClient.getClientData(getClientId(authentication)).getBody();
    }

    @Override
    public RequestNewRealEstatePolicyToInsuranceService getAllFieldsAndSendRequestToService(Authentication authentication,
                                                                                            RequestNewRealEstatePolicyFromUser requestNewRealEstatePolicyFromUser) {
        ResponseClientDataDto responseClientDataDto = getUserInformation(authentication);
        SuggestionDto.Suggestions suggestions = getAddressParameters(requestNewRealEstatePolicyFromUser.getLocation()).getBody();
        assert suggestions!=null;
        SuggestionDto suggestionDto = suggestions.getSuggestions().remove(0);
        return insuranceDtoMapper.toRealEstatePolicyToInsuranceService(requestNewRealEstatePolicyFromUser,suggestionDto,responseClientDataDto);
    }


    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

}
