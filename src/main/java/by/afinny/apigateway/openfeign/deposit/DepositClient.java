package by.afinny.apigateway.openfeign.deposit;

import by.afinny.apigateway.dto.deposit.ActiveDepositDto;
import by.afinny.apigateway.dto.deposit.DepositDto;
import by.afinny.apigateway.dto.deposit.RequestAutoRenewalDto;
import by.afinny.apigateway.dto.deposit.WithdrawDepositDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("DEPOSIT/auth/deposits")
public interface DepositClient {

    @GetMapping
    ResponseEntity<List<ActiveDepositDto>> getActiveDeposits(@RequestParam UUID clientId);

    @PatchMapping("{agreementId}/revocation")
    ResponseEntity<Void> earlyWithdrawalDeposit(@RequestParam UUID clientId,
                                                @PathVariable UUID agreementId,
                                                @RequestBody WithdrawDepositDto withdrawDepositDto);

    @GetMapping("{agreementId}")
    ResponseEntity<DepositDto> getDeposit(@RequestParam UUID clientId,
                                          @PathVariable UUID agreementId,
                                          @RequestParam UUID cardId);

    @PatchMapping("/{agreementId}/auto-renewal")
    ResponseEntity<Void> updateAutoRenewal(@RequestParam UUID clientId,
                                           @PathVariable("agreementId") UUID agreementId,
                                           @RequestBody RequestAutoRenewalDto debitAutoRenewalDto);
}
