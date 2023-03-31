package by.afinny.apigateway.openfeign.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CreditCardStatementDto;
import by.afinny.apigateway.dto.moneytransfer.DebitCardStatementDto;
import by.afinny.apigateway.dto.moneytransfer.DetailsHistoryDto;
import by.afinny.apigateway.dto.moneytransfer.FilterOptionsDto;
import by.afinny.apigateway.dto.moneytransfer.TransferOrderHistoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@FeignClient("MONEY-TRANSFER/auth/history")
public interface TransferHistoryClient {

    @GetMapping
    ResponseEntity<List<TransferOrderHistoryDto>> getTransferOrderHistory(@SpringQueryMap @Valid FilterOptionsDto filterOptionsDto);

    @GetMapping("credit/{cardId}")
    ResponseEntity<List<CreditCardStatementDto>> getCreditCardStatement(@RequestParam UUID clientId,
                                                                        @PathVariable UUID cardId,
                                                                        @RequestParam String from,
                                                                        @RequestParam String to,
                                                                        @RequestParam Integer pageNumber,
                                                                        @RequestParam Integer pageSize);

    @GetMapping("/details")
    ResponseEntity<DetailsHistoryDto> getDetailsHistory(@RequestParam UUID clientId, @RequestParam UUID transferOrderId);

    @GetMapping("deposit/{cardId}")
    ResponseEntity<List<DebitCardStatementDto>> getViewDebitCardStatement(@RequestParam UUID clientId,
                                                                          @PathVariable UUID cardId,
                                                                          @RequestParam String from,
                                                                          @RequestParam String to,
                                                                          @RequestParam Integer pageNumber,
                                                                          @RequestParam Integer pageSize);
}


