package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.MedicinePolicyFromUserDto;
import by.afinny.apigateway.dto.insurance.NewPolicyFromUserDto;
import by.afinny.apigateway.dto.insurance.RequestMedicinePolicyDto;
import by.afinny.apigateway.dto.insurance.RequestNewPolicy;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyFromUser;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyToInsuranceService;
import by.afinny.apigateway.dto.insurance.RequestTravelPolicyDto;
import by.afinny.apigateway.dto.insurance.ResponseTravelPolicyDto;
import by.afinny.apigateway.dto.insurance.SuggestionDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.exception.NotEqualClientIdException;
import by.afinny.apigateway.mapper.InsuranceDtoMapper;
import by.afinny.apigateway.openfeign.insurance.InsuranceOrderClient;
import by.afinny.apigateway.service.insurance.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/insurance-order")
@Tag(name = "Insurance-Order Controller", description = "Getting data about insurance-orders")
public class InsuranceOrderController {

    private final InsuranceService insuranceService;
    private final InsuranceOrderClient insuranceOrderClient;
    private final InsuranceDtoMapper insuranceDtoMapper;

    @Operation(summary = "Create new policy", description = "Registration of a car insurance policy")
    @ApiResponse(responseCode = "200", description = "Policy was successfully created")
    @PostMapping("/new-cars")
    public ResponseEntity<RequestNewPolicy> createNewCarPolicy(Authentication authentication,
                                                               @RequestBody NewPolicyFromUserDto newPolicyFromUserDto) {
        ResponseClientDataDto responseClientDataDto = insuranceService.getUserInformation(authentication);
        SuggestionDto.Suggestions suggestions = insuranceService.getAddressParameters(newPolicyFromUserDto.getRegion()).getBody();
        assert suggestions != null;
        SuggestionDto suggestionDto = suggestions.getSuggestions().remove(0);
        RequestNewPolicy requestNewPolicy = insuranceDtoMapper.toPolicyDto(newPolicyFromUserDto, responseClientDataDto, suggestionDto);
        return insuranceOrderClient.createNewPolicy(requestNewPolicy);
    }

    @Operation(summary = "Create medicine policy", description = "Create medicine policy at the client's request")
    @ApiResponse(responseCode = "200", description = "Medicine policy created",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = RequestMedicinePolicyDto.class))})
    @PostMapping("/new-medicine")
    public ResponseEntity<RequestMedicinePolicyDto> registerNewMedicinePolicy(Authentication authentication,
                                                          @RequestBody MedicinePolicyFromUserDto medicinePolicyFromUserDto) {
        ResponseClientDataDto responseClientDataDto = insuranceService.getUserInformation(authentication);
        SuggestionDto.Suggestions suggestions = insuranceService.getAddressParameters(medicinePolicyFromUserDto.getRegion()).getBody();
        assert suggestions != null;
        SuggestionDto suggestionDto = suggestions.getSuggestions().remove(0);
        RequestMedicinePolicyDto medicinePolicyDto = insuranceDtoMapper.toMedicinePolicyDto(medicinePolicyFromUserDto,
                responseClientDataDto, suggestionDto);
        return insuranceOrderClient.registerNewMedicinePolicy(medicinePolicyDto);
    }

    @Operation(summary = "Create real estate policy", description = "Create real estate policy at the client's request")
    @ApiResponse(responseCode = "200", description = "Real estate created",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = RequestNewRealEstatePolicyFromUser.class))})
    @PostMapping("/new-property")
    public ResponseEntity<Void> createNewRealEstatePolicy(@RequestBody RequestNewRealEstatePolicyFromUser estatePolicy,
                                                          Authentication authentication) {
          RequestNewRealEstatePolicyToInsuranceService policyToInsuranceService = insuranceService.getAllFieldsAndSendRequestToService(authentication,estatePolicy);
        return insuranceOrderClient.createNewRealEstatePolicy(policyToInsuranceService);
    }

    @Operation(summary = "Create travel policy", description = "Create travel policy at the client's request")
    @ApiResponse(responseCode = "200", description = "Travel policy created",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = RequestTravelPolicyDto.class))})
    @PostMapping("/new-travel-program")
    public ResponseEntity<ResponseTravelPolicyDto> createNewTravelPolicy
            (Authentication authentication, @RequestBody RequestTravelPolicyDto requestTravelPolicyDto) {
        ResponseClientDataDto responseClientDataDto = insuranceService.getUserInformation(authentication);
        if (responseClientDataDto.getClientId().equals(requestTravelPolicyDto.getClientId())) {
            return insuranceOrderClient.createNewTravelPolicy(requestTravelPolicyDto);
        } else throw new NotEqualClientIdException();
    }
}
