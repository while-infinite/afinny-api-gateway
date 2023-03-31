package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.CardInfoDto;
import by.afinny.apigateway.dto.credit.CreditCardDto;
import by.afinny.apigateway.dto.credit.CreditCardLimitDto;
import by.afinny.apigateway.dto.credit.CreditCardPinCodeDto;
import by.afinny.apigateway.dto.credit.RequestCardStatusDto;
import by.afinny.apigateway.openfeign.credit.CreditCardClient;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/credit-cards")
@Tag(name = "CreditCard Controller", description = "Manage actions with credit cards")
public class CreditCardController {

    public final static String URL_CARDS = "/api/v1/credit-cards";
    public final static String LIMIT_URL = "/api/v1/credit-cards/limit";
    public final static String DELETE_URL = "/api/v1/credit-cards";
    public final static String PARAM_CARD_ID = "cardId";
    public static final String URL_CARDS_CLIENT_ID = "/api/v1/credit-cards/info";
    public static final String URL_CARD_PIN_CODE = "/code";

    private final CreditCardClient creditCardClient;

    @Operation(summary = "Getting information about credit cards", description = "Get information about about the client's credit cards")
    @ApiResponse(responseCode = "200", description = "Successfully found credit cards and return information about credit cards",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreditCardDto.class))})
    @GetMapping
    public ResponseEntity<List<CreditCardDto>> getCreditCards(Authentication authentication) {
        return creditCardClient.getCreditCards(getClientId(authentication));
    }

    @Operation(summary = "Change card status", description = "Change card status")
    @ApiResponse(responseCode = "200", description = "Card status has been updated",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RequestCardStatusDto.class))})
    @PatchMapping("active-cards")
    public ResponseEntity<Void> getCreditCards(Authentication authentication, @RequestBody RequestCardStatusDto requestCardStatusDto) {
        return creditCardClient.setNewCardStatus(getClientId(authentication), requestCardStatusDto);
    }

    @Operation(summary = "Get credit card data", description = "Getting data about the balance and credit limit of a credit card")
    @ApiResponse(responseCode = "200", description = "Found the card and return balance",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = CardInfoDto.class))})
    @GetMapping("info")
    public ResponseEntity<CardInfoDto> getCardInformation(Authentication authentication, @RequestParam UUID cardId) {
        return creditCardClient.getCardInformation(getClientId(authentication), cardId);
    }

    @Operation(summary = "Setting credit card limit", description = "Changing an existing credit card limit or setting if it has not been set")
    @ApiResponse(responseCode = "200", description = "Credit card limit has been successfully changed")
    @PatchMapping("limit")
    public ResponseEntity<Void> setCreditCardLimit(Authentication authentication, @RequestBody CreditCardLimitDto creditCardLimitDto) {
        return creditCardClient.setCreditCardLimit(getClientId(authentication), creditCardLimitDto);
    }

    @Operation(summary = "Change credit card pin code", description = "Changing an existing credit card pin code")
    @ApiResponse(responseCode = "200", description = "Credit card pin code has been successfully changed")
    @PostMapping("code")
    public ResponseEntity<Void> changeCardPinCode(Authentication authentication, @RequestBody CreditCardPinCodeDto creditCardPinCodeDto) {
        return creditCardClient.changeCardPinCode(getClientId(authentication), creditCardPinCodeDto);
    }

    @Operation(summary = "Deleting credit card",
            description = "Deleting credit card if balance equals credit limit and card exists")
    @ApiResponse(responseCode = "200", description = "Credit card has been deleted")
    @DeleteMapping
    public ResponseEntity<Void> deleteCreditCard(Authentication authentication, @RequestParam UUID cardId) {
        return creditCardClient.deleteCreditCard(getClientId(authentication), cardId);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
