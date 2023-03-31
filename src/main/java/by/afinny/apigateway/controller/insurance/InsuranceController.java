package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.PolicyInfoDto;
import by.afinny.apigateway.dto.insurance.ResponseApplicationInsuranceTypeDto;
import by.afinny.apigateway.dto.insurance.ResponsePaymentDetailsDto;
import by.afinny.apigateway.dto.insurance.ResponseRejectionLetterDto;
import by.afinny.apigateway.dto.insurance.ResponseUserPolicyDto;
import by.afinny.apigateway.dto.insurance.SuggestionDto;
import by.afinny.apigateway.openfeign.insurance.InsuranceClient;
import by.afinny.apigateway.service.insurance.InsuranceService;
import by.afinny.apigateway.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/insurance")
@Tag(name = "Insurance Controller", description = "Getting data about insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final InsuranceClient insuranceClient;

    @Operation(summary = "Displaying Address Parameters", description = "Displaying Address Parameters by users query")
    @ApiResponse(responseCode = "200", description = "Received address parameters",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = SuggestionDto.Suggestions.class))})
    @GetMapping("address")
    public ResponseEntity<SuggestionDto.Suggestions> getAddressParameters(@RequestParam String query) {
        return insuranceService.getAddressParameters(query);
    }

    @Operation(summary = "Get policy information", description = "Getting main information about insurance policy")
    @ApiResponse(responseCode = "200", description = "Found insurance policy and return information",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = PolicyInfoDto.class))})
    @GetMapping("/{applicationId}")
    ResponseEntity<PolicyInfoDto> getPolicyInformation(Authentication authentication,
                                                       @PathVariable UUID applicationId) {
        System.out.println(Utils.getClientId(authentication));
        return insuranceClient.getPolicyInformation(Utils.getClientId(authentication), applicationId);
    }

    @Operation(summary = "Getting insurance user policies", description = "Get information about  list of insurance user policies")
    @ApiResponse(responseCode = "200", description = "Found insurance user policies",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseUserPolicyDto.class))})
    @GetMapping
    public ResponseEntity<List<ResponseUserPolicyDto>> getUserPolicies(Authentication authentication) {
        return insuranceClient.getUserPolicies(Utils.getClientId(authentication));
    }

    @Operation(summary = "Getting insurance types", description = "Get information about  list of insurance types")
    @ApiResponse(responseCode = "200", description = "Found insurance types",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseApplicationInsuranceTypeDto.class))})
    @GetMapping("types")
    public ResponseEntity<List<ResponseApplicationInsuranceTypeDto>> getInsuranceTypes() {
        return insuranceClient.getInsuranceTypes();
    }

    @Operation(summary = "Getting rejection letter", description = "Get information about rejection")
    @ApiResponse(responseCode = "200", description = "Found  rejection letter",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseRejectionLetterDto.class))})
    @GetMapping("/{application-id}/report")
    public ResponseEntity<ResponseRejectionLetterDto> getRejectionLetter(Authentication authentication,
                                                                         @PathVariable("application-id") UUID applicationId) {
        return insuranceClient.getRejectionLetter(Utils.getClientId(authentication), applicationId);
    }

    @Operation(summary = "Getting payment details", description = "Get information about payment details")
    @ApiResponse(responseCode = "200", description = "Found  payment details",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponsePaymentDetailsDto.class))})
    @GetMapping("/{application-id}/payment-details")
    public ResponseEntity<ResponsePaymentDetailsDto> getPaymentDetails(Authentication authentication,
                                                                       @PathVariable("application-id") UUID applicationId) {
        return insuranceClient.getPaymentDetails(Utils.getClientId(authentication), applicationId);

    }

    @Operation(summary = "Cancel policy application", description = "Cancellation of an application for an insurance policy")
    @ApiResponse(responseCode = "200", description = "Application was cancelled")
    @DeleteMapping("/{application-id}/revocation")
    public ResponseEntity<Void> cancelPolicyApplication(Authentication authentication,
                                                        @PathVariable("application-id") UUID applicationId) {
        return insuranceClient.cancelPolicyApplication(Utils.getClientId(authentication), applicationId);
    }

}