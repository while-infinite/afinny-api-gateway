package by.afinny.apigateway.openfeign.deposit;

import by.afinny.apigateway.dto.deposit.AccountWithCardInfoDto;
import by.afinny.apigateway.dto.deposit.CardDebitLimitDto;
import by.afinny.apigateway.dto.deposit.DebitCardInfoDto;
import by.afinny.apigateway.dto.deposit.DepositCardStatusDto;
import by.afinny.apigateway.dto.deposit.NewPinCodeDebitCardDto;
import by.afinny.apigateway.dto.deposit.ViewCardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.UUID;

@FeignClient("DEPOSIT/auth/deposit-cards")
public interface CardsClient {

    @GetMapping("{cardId}")
    ResponseEntity<ViewCardDto> getAccountByCardId(@RequestParam UUID clientId, @PathVariable UUID cardId);

    @GetMapping
    ResponseEntity<List<AccountWithCardInfoDto>> getActiveProducts(@RequestParam UUID clientId);

    @PatchMapping("/active-cards")
    ResponseEntity<Void> setNewCardStatus(@RequestParam UUID clientId, @RequestBody DepositCardStatusDto cardStatus);

    @PatchMapping("/limit")
    ResponseEntity<Void> setDebitCardLimit(@RequestParam UUID clientId, @RequestBody CardDebitLimitDto cardDebitLimitDto);

    @DeleteMapping("{cardId}")
    ResponseEntity<Void> deleteDebitCard(@RequestParam UUID clientId, @PathVariable UUID cardId);

    @PostMapping("/code")
    ResponseEntity<Void> changePinCodeDebitCard(@RequestParam UUID clientId, @RequestBody NewPinCodeDebitCardDto newPinCodeDebitCardDto);

    @GetMapping("{cardId}/info")
    ResponseEntity<DebitCardInfoDto> getCardInformation(@RequestParam UUID clientId, @PathVariable UUID cardId);
}