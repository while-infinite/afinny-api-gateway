package by.afinny.apigateway.openfeign.credit;

import by.afinny.apigateway.dto.credit.CreditBalanceDto;
import by.afinny.apigateway.dto.credit.CreditDto;
import by.afinny.apigateway.dto.credit.CreditScheduleDto;
import by.afinny.apigateway.dto.credit.DetailsDto;
import by.afinny.apigateway.dto.credit.ResponseOperationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient("CREDIT/auth/credits")
public interface CreditClient {

    @GetMapping
    ResponseEntity<List<CreditDto>> getClientCreditsWithActiveStatus(@RequestParam UUID clientId);

    @GetMapping("/{creditId}")
    ResponseEntity<CreditBalanceDto> getCreditBalance(@RequestParam UUID clientId, @PathVariable UUID creditId);

    @GetMapping("/{creditId}/schedule")
    ResponseEntity<CreditScheduleDto> getPaymentSchedule(@RequestParam UUID clientId, @PathVariable UUID creditId);

    @GetMapping("/{agreementId}/details")
    ResponseEntity<DetailsDto> getDetailsForPayment(@RequestParam UUID clientId, @PathVariable UUID agreementId);
    @GetMapping("/{creditId}/history")
    ResponseEntity<List<ResponseOperationDto>> getDetailsOfLastPayments(@RequestParam UUID clientId,
                                                                        @PathVariable UUID creditId,
                                                                        @RequestParam Integer pageNumber,
                                                                        @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize);
}
