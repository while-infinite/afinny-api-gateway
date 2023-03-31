package by.afinny.apigateway.openfeign.credit;

import by.afinny.apigateway.dto.credit.CardInfoDto;
import by.afinny.apigateway.dto.credit.CreditCardDto;
import by.afinny.apigateway.dto.credit.CreditCardLimitDto;
import by.afinny.apigateway.dto.credit.CreditCardPinCodeDto;
import by.afinny.apigateway.dto.credit.RequestCardStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.UUID;

@FeignClient("CREDIT/auth/credit-cards")
public interface CreditCardClient {

    @GetMapping
    ResponseEntity<List<CreditCardDto>> getCreditCards(@RequestParam UUID clientId);

    @PatchMapping("active-cards")
    ResponseEntity<Void> setNewCardStatus(@RequestParam UUID clientId, @RequestBody RequestCardStatusDto requestCardStatusDto);

    @GetMapping("info")
    ResponseEntity<CardInfoDto> getCardInformation(@RequestParam UUID clientId, @RequestParam UUID cardId);

    @PatchMapping("limit")
    ResponseEntity<Void> setCreditCardLimit(@RequestParam UUID clientId, @RequestBody CreditCardLimitDto creditCardLimitDto);

    @PostMapping("code")
    ResponseEntity<Void> changeCardPinCode(@RequestParam UUID clientId, @RequestBody CreditCardPinCodeDto creditCardPinCodeDto);

    @DeleteMapping()
    ResponseEntity<Void> deleteCreditCard(@RequestParam UUID clientId, @RequestParam UUID cardId);
}
