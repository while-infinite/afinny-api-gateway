package by.afinny.apigateway.openfeign.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.ChangeStatusResponseDto;
import by.afinny.apigateway.dto.moneytransfer.IsFavoriteTransferDto;
import by.afinny.apigateway.dto.moneytransfer.RequestRefillBrokerageAccountDto;
import by.afinny.apigateway.dto.moneytransfer.ResponseBrokerageAccountDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentResponseDto;
import by.afinny.apigateway.dto.moneytransfer.TransferDto;
import by.afinny.apigateway.dto.moneytransfer.TransferOrderIdDto;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
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

@FeignClient("MONEY-TRANSFER/auth/payments")
public interface TransferClient {

    @DeleteMapping("/{transferId}/draft")
    ResponseEntity<Void> deleteIdDraftTransfers(@RequestParam UUID clientId,
                                                @PathVariable UUID transferId);

    @PatchMapping("/favorites")
    ResponseEntity<IsFavoriteTransferDto> getFavoriteTransfers(@RequestParam UUID clientId,
                                                               @RequestBody TransferOrderIdDto transferOrderIdDto);

    @GetMapping("favorites/{transferOrderId}")
    ResponseEntity<TransferDto> getFavoriteTransfers(@RequestParam UUID clientId,
                                                     @PathVariable UUID transferOrderId);

    @GetMapping("/transferType")
    ResponseEntity<List<TransferTypeName>> getTransferType();

    @GetMapping("/paymentType")
    ResponseEntity<List<TransferTypeName>> getPaymentType();

    @PostMapping("/new")
    ResponseEntity<ResponseBrokerageAccountDto> refillBrokerageAccount(@RequestParam UUID clientId,
                                                                       @RequestBody RequestRefillBrokerageAccountDto requestRefillBrokerageAccountDto);

    @PostMapping("/new-payment")
    ResponseEntity<CreatePaymentResponseDto> createPaymentOrTransfer(@RequestParam UUID clientId,
                                                                     @RequestBody CreatePaymentDto createPaymentDto);

    @PatchMapping("/{transferId}/status")
    ResponseEntity<ChangeStatusResponseDto> changeStatus(@PathVariable UUID transferId);
}