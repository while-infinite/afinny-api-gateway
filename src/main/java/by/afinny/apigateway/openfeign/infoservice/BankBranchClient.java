package by.afinny.apigateway.openfeign.infoservice;

import by.afinny.apigateway.dto.infoservice.ResponseBankBranchDto;
import by.afinny.apigateway.dto.infoservice.ResponseBranchCoordinatesDto;
import by.afinny.apigateway.dto.infoservice.constant.BankBranchType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("INFO-SERVICE/bank-branch")
public interface BankBranchClient {

    @GetMapping
    ResponseEntity<List<ResponseBankBranchDto>> getAllBankBranches();

    @GetMapping("/filters")
    ResponseEntity<List<ResponseBranchCoordinatesDto>> getFilteredBankBranches(@RequestParam BankBranchType bankBranchType,
                                                                               @RequestParam Boolean closed,
                                                                               @RequestParam Boolean workAtWeekends,
                                                                               @RequestParam Boolean cashWithdraw,
                                                                               @RequestParam Boolean moneyTransfer,
                                                                               @RequestParam Boolean acceptPayment,
                                                                               @RequestParam Boolean currencyExchange,
                                                                               @RequestParam Boolean exoticCurrency,
                                                                               @RequestParam Boolean ramp,
                                                                               @RequestParam Boolean replenishCard,
                                                                               @RequestParam Boolean replenishAccount,
                                                                               @RequestParam Boolean consultation,
                                                                               @RequestParam Boolean insurance,
                                                                               @RequestParam Boolean replenishWithoutCard);
}
