package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.ChangeStatusResponseDto;
import by.afinny.apigateway.dto.moneytransfer.IsFavoriteTransferDto;
import by.afinny.apigateway.dto.moneytransfer.RequestRefillBrokerageAccountDto;
import by.afinny.apigateway.dto.moneytransfer.ResponseBrokerageAccountDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentResponseDto;
import by.afinny.apigateway.dto.moneytransfer.TransferDto;
import by.afinny.apigateway.dto.moneytransfer.TransferOrderIdDto;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import by.afinny.apigateway.openfeign.moneytransfer.TransferClient;
import by.afinny.apigateway.service.moneytransfer.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@RequestMapping("api/v1/payments")
@Tag(name = "Transfer Controller", description = "Get favorite payment")
public class TransferController {

    private final TransferClient transferClient;
    private final TransferService transferService;

    public static final String URL_TRANSFER = "/api/v1/payments";
    public static final String URL_DELETE_TRANSFER = "/{transferId}/draft";
    public static final String FAVORITES_URL = "/favorites";
    public static final String FAVORITES_TRANSFER_URL = "/{transferOrderId}";
    public static final String URL_TRANSFER_TYPE = "/transferType";
    public static final String URL_PAYMENT_TYPE = "/paymentType";
    public static final String NEW = "/new";
    public static final String URL_CHANGE_STATUS = "/{transferId}/status";




    @Operation(summary = "Getting favorite payment", description = "Get favorite payment")
    @ApiResponse(responseCode = "200", description = "Found the favorite payment",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = TransferDto.class))})
    @GetMapping("favorites/{transferOrderId}")
    public ResponseEntity<TransferDto> getFavoriteTransfers(Authentication authentication,
                                                            @PathVariable UUID transferOrderId) {

        return transferClient.getFavoriteTransfers(getClientId(authentication), transferOrderId);
    }

    @Operation(summary = "Getting information about a draft", description = "Get information about delete draft")
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("/{transferId}/draft")
    public ResponseEntity<Void> deleteActualDraftTransfers(Authentication authentication,
                                                           @PathVariable UUID transferId) {
        return transferClient.deleteIdDraftTransfers(getClientId(authentication),transferId);
    }

    @Operation(summary = "Getting information about a completed payment",
               description = "Get information about the selected payment")
    @ApiResponse(responseCode = "200", description = "Favorite payment found",
                 content = {@Content(mediaType = "application/json",
                         array = @ArraySchema(schema = @Schema(implementation = IsFavoriteTransferDto.class)))})
    @PatchMapping("/favorites")
    public ResponseEntity<IsFavoriteTransferDto> getActualFavoriteTransfers(Authentication authentication,
                                                                            @RequestBody TransferOrderIdDto transferOrderIdDto) {
        return transferClient.getFavoriteTransfers(getClientId(authentication),transferOrderIdDto);
    }

    @Operation(summary = "Getting transfer type name", description = "Cet transfer type name")
    @ApiResponse(responseCode = "200", description = "Found transfer type name",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = TransferDto.class))})
    @GetMapping("/transferType")
    public ResponseEntity<List<TransferTypeName>> getTransferType() {
        return transferClient.getTransferType();
    }

    @Operation(summary = "Getting payment type name", description = "Get payment type name")
    @ApiResponse(responseCode = "200", description = "Found payment type name",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = TransferDto.class))})
    @GetMapping("/paymentType")
    public ResponseEntity<List<TransferTypeName>> getPaymentType() {
        return transferClient.getPaymentType();
    }

    @Operation(summary = "Refill brokerage account", description = "Refill brokerage account")
    @ApiResponse(responseCode = "200", description = "Refill brokerage account", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseBrokerageAccountDto.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    @PostMapping("/new")
    public ResponseEntity<ResponseBrokerageAccountDto> refillBrokerageAccount(Authentication authentication,
                                                                              @RequestBody RequestRefillBrokerageAccountDto requestRefillBrokerageAccountDto) {
        return transferClient.refillBrokerageAccount(getClientId(authentication), requestRefillBrokerageAccountDto);
    }

    @Operation(summary = "Create payment or transfer", description = "Create new payment or transfer and get info about it")
    @ApiResponse(responseCode = "200", description = "Get info about new payment (transfer)",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreatePaymentDto.class))})
    @PostMapping("/new-payment")
    public ResponseEntity<CreatePaymentResponseDto> createPaymentOrTransfer(Authentication authentication,
                                                                            @RequestBody CreatePaymentDto createPaymentDto) {
        return ResponseEntity.ok(transferService.createPaymentOrTransfer(getClientId(authentication), createPaymentDto));
    }

    @Operation(summary = "Change transferId status ", description = "Change transferId status")
    @ApiResponse(responseCode = "200", description = "Change transferId status",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ChangeStatusResponseDto.class))})
    @PatchMapping("/{transferId}/status")
    public ResponseEntity<ChangeStatusResponseDto> changeStatus(@PathVariable UUID transferId) {
        return transferClient.changeStatus(transferId);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
