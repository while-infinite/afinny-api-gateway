package by.afinny.apigateway.controller.investments;

import by.afinny.apigateway.dto.investments.RequestNewAccountAgreeDto;
import by.afinny.apigateway.dto.investments.ResponseNewAccountAgreeDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.mapper.investments.NewAccountAgreeDtoMapper;
import by.afinny.apigateway.openfeign.investments.OrderClient;
import by.afinny.apigateway.openfeign.userservice.InformationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/investment-order")
@Tag(name = "Investment Order Controller", description = "Manage actions withs investment orders")
public class OrderController {

    private final InformationClient informationClient;
    private final OrderClient orderClient;
    private final NewAccountAgreeDtoMapper newAccountAgreeDtoMapper;

    @Operation(summary = "Create new account agree",
               description = "Create new account agree for the creation of a brokerage deposit")
    @ApiResponse(responseCode = "200", description = "Account agree created",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseNewAccountAgreeDto.class))})
    @PostMapping("/new-account")
    public ResponseEntity<ResponseNewAccountAgreeDto> createNewAccount(Authentication authentication) {
        UUID clientId = UUID.fromString(authentication.getName());

        ResponseClientDataDto responseClientDataDto = informationClient.getClientData(clientId).getBody();
        RequestNewAccountAgreeDto requestNewAccountAgreeDto = newAccountAgreeDtoMapper.toRequestNewAccountAgree(responseClientDataDto);

        ResponseNewAccountAgreeDto responseNewAccountAgreeDto = orderClient.createAccountAgree(requestNewAccountAgreeDto).getBody();

        return ResponseEntity.ok(responseNewAccountAgreeDto);
    }
}
