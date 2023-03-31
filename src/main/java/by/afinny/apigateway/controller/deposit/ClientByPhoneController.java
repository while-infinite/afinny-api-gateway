package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.ClientDto;
import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
import by.afinny.apigateway.openfeign.deposit.FindByPhoneClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static by.afinny.apigateway.util.Utils.getClientId;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Find client by phone controller", description = "Find client by phone")
public class ClientByPhoneController {

    public final static String URL_CLIENT_BY_PHONE = "/api/v1/accounts";

    private final FindByPhoneClient findByPhoneClient;

    @Operation(summary = "Find client by mobile phone", description = "Find client by mobile phone")
    @ApiResponse(responseCode = "200", description = "Client were found", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ClientDto.class))})
    @GetMapping
    public ResponseEntity<ClientDto> getClientByPhone(Authentication authentication,
                                                      @RequestParam(name = "mobilePhone") String mobilePhone,
                                                      @RequestParam(name = "currency_code") CurrencyCode currencyCode) {
        System.out.println("getClientByPhone invoke");
        return findByPhoneClient.getClientByPhone(getClientId(authentication), mobilePhone, currencyCode);
    }
}
