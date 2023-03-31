package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.credit.CardInfoDto;
import by.afinny.apigateway.dto.deposit.AccountWithCardInfoDto;
import by.afinny.apigateway.dto.deposit.CardDebitLimitDto;
import by.afinny.apigateway.dto.deposit.DebitCardInfoDto;
import by.afinny.apigateway.dto.deposit.DepositCardStatusDto;
import by.afinny.apigateway.dto.deposit.NewPinCodeDebitCardDto;
import by.afinny.apigateway.dto.deposit.ViewCardDto;
import by.afinny.apigateway.openfeign.deposit.CardsClient;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/deposit-cards")
@Tag(name = "Cards Controller", description = "Manage actions with active cards")
public class CardController {

    public static final String URL_CARDS = "/api/v1/deposit-cards/";
    public static final String URL_CARD_ID = "/{cardId}";
    public static final String URL_LIMIT = "/limit";
    public static final String URL_CARD_PIN_CODE = "/code";
    public static final String URL_ACTIVE_CARDS = "/active-cards";


    private final CardsClient cardsClient;

    @Operation(summary = "Getting active cards", description = "Get information about active cards")
    @ApiResponse(responseCode = "200", description = "Found the active cards",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountWithCardInfoDto.class))})
    @GetMapping
    public ResponseEntity<List<AccountWithCardInfoDto>> getActiveProducts(Authentication authentication) {
        return cardsClient.getActiveProducts(getClientId(authentication));
    }

    @Operation(summary = "Change debit card status", description = "Change debit card status")
    @ApiResponse(responseCode = "200", description = "Debit card status has been successfully changed")
    @PatchMapping("/active-cards")
    public ResponseEntity<Void> setNewCardStatus(Authentication authentication,
                                                 @RequestBody DepositCardStatusDto cardStatus) {
        return cardsClient.setNewCardStatus(getClientId(authentication), cardStatus);
    }

    @Operation(summary = "Checking the recipient", description = "Checking the recipient by card number")
    @ApiResponse(responseCode = "200", description = "Found the recipient by card number",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ViewCardDto.class))})
    @GetMapping("{cardId}")
    public ResponseEntity<ViewCardDto> getAccountByCardId(Authentication authentication,
                                                          @PathVariable UUID cardId) {
        return cardsClient.getAccountByCardId(getClientId(authentication), cardId);
    }

    @Operation(summary = "Change debit card limit", description = "Change debit card limit")
    @ApiResponse(responseCode = "200", description = "Debit card limit has been successfully changed")
    @PatchMapping("/limit")
    public ResponseEntity<Void> setDebitCardLimit(Authentication authentication,
                                                  @RequestBody CardDebitLimitDto cardDebitLimitDto) {
        return cardsClient.setDebitCardLimit(getClientId(authentication), cardDebitLimitDto);
    }

    @Operation(summary = "Deleting debit card", description = "Deleting debit card")
    @ApiResponse(responseCode = "200", description = "Debit card has been deleted")
    @DeleteMapping("{cardId}")
    public ResponseEntity<Void> deleteDebitCard(Authentication authentication,
                                                @PathVariable UUID cardId) {
        return cardsClient.deleteDebitCard(getClientId(authentication), cardId);
    }

    @Operation(summary = "Change pin-code debit card", description = "Change pin-code debit card")
    @ApiResponse(responseCode = "200", description = "Pin-code debit card has been successfully changed")
    @PostMapping("/code")
    ResponseEntity<Void> changePinCodeDebitCard(Authentication authentication,
                                                @RequestBody NewPinCodeDebitCardDto newPinCodeDebitCardDto) {
        return cardsClient.changePinCodeDebitCard(getClientId(authentication), newPinCodeDebitCardDto);
    }

    @Operation(summary = "Get credit card data", description = "Getting data about the balance and credit limit of a credit card")
    @ApiResponse(responseCode = "200", description = "Found the card and return balance",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = CardInfoDto.class))})
    @GetMapping("{cardId}/info")
    public ResponseEntity<DebitCardInfoDto> getCardInformation(Authentication authentication,
                                                               @PathVariable UUID cardId) {
        return cardsClient.getCardInformation(getClientId(authentication), cardId);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}