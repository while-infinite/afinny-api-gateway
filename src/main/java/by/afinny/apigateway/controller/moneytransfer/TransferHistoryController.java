package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CreditCardStatementDto;
import by.afinny.apigateway.dto.moneytransfer.DebitCardStatementDto;
import by.afinny.apigateway.dto.moneytransfer.DetailsHistoryDto;
import by.afinny.apigateway.dto.moneytransfer.FilterOptionsDto;
import by.afinny.apigateway.dto.moneytransfer.TransferOrderHistoryDto;
import by.afinny.apigateway.openfeign.moneytransfer.TransferHistoryClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/history")
@Tag(name = "TransferHistory Controller", description = "Manage actions with commission")
public class TransferHistoryController {

    public final static String HISTORY_URL = "/api/v1/history";
    public final static String CREDIT_URL = "/credit/{cardId}";
    public final static String DEPOSIT_URL = "/deposit/{cardId}";
    public final static String FROM_PARAM = "from";
    public final static String TO_PARAM = "to";
    public final static String CLIENT_ID_PARAM = "authentication";
    public final static String PAGE_NUMBER_PARAM = "pageNumber";
    public final static String PAGE_SIZE_PARAM = "pageSize";
    public final static String DETAILS_URL = "/details";

    private final TransferHistoryClient transferHistoryClient;

    @Operation(summary = "Get history", description = "Get transfer order history")
    @ApiResponse(responseCode = "200", description = "Found the transfer order history",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = TransferOrderHistoryDto.class))})
    @GetMapping()
    public ResponseEntity<List<TransferOrderHistoryDto>> getTransferOrderHistory(Authentication authentication,
                                                                                 @Valid FilterOptionsDto filterOptionsDto) {

        filterOptionsDto.setClientId(getClientId(authentication));
        return transferHistoryClient.getTransferOrderHistory(filterOptionsDto);
    }

    @Operation(summary = "Getting history details transfer order", description = "Get history details transfer order")
    @ApiResponse(responseCode = "200", description = "Found the history details transfer order",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = DetailsHistoryDto.class))})
    @GetMapping("/details")
    ResponseEntity<DetailsHistoryDto> getDetailsHistory(Authentication authentication,
                                                        @RequestParam UUID transferOrderId) {
        return transferHistoryClient.getDetailsHistory(getClientId(authentication), transferOrderId);
    }

    @Operation(summary = "Get statement", description = "Get credit card statement")
    @ApiResponse(responseCode = "200", description = "Found the credit card statement",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = CreditCardStatementDto.class))})
    @GetMapping("credit/{cardId}")
    public ResponseEntity<List<CreditCardStatementDto>> getCreditCardStatement(Authentication authentication,
                                                                               @PathVariable UUID cardId,
                                                                               @RequestParam @Parameter(description = "Пример ввода: 2023-03-21 00:00:00") String from,
                                                                               @RequestParam @Parameter(description = "Пример ввода: 2023-03-21 00:00:00") String to,
                                                                               @RequestParam Integer pageNumber,
                                                                               @RequestParam Integer pageSize) {

        return transferHistoryClient.getCreditCardStatement(getClientId(authentication), cardId, from, to, pageNumber, pageSize);
    }

    @Operation(summary = "Get history", description = "Get transfer order history")
    @ApiResponse(responseCode = "200", description = "Found the transfer order history",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = DebitCardStatementDto.class))})
    @GetMapping("deposit/{cardId}")
    ResponseEntity<List<DebitCardStatementDto>> getViewDebitCardStatement(Authentication authentication,
                                                                          @PathVariable UUID cardId,
                                                                          @RequestParam @Parameter(description = "Пример ввода: 2023-03-21 00:00:00") String from,
                                                                          @RequestParam @Parameter(description = "Пример ввода: 2023-03-21 00:00:00") String to,
                                                                          @RequestParam Integer pageNumber,
                                                                          @RequestParam Integer pageSize) {
        return transferHistoryClient.getViewDebitCardStatement(getClientId(authentication),cardId, from, to, pageNumber, pageSize);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
