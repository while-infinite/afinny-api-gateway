package by.afinny.apigateway.controller.infoservice;

import by.afinny.apigateway.dto.infoservice.ResponseBankBranchDto;
import by.afinny.apigateway.dto.infoservice.ResponseBranchCoordinatesDto;
import by.afinny.apigateway.dto.infoservice.constant.BankBranchType;
import by.afinny.apigateway.openfeign.infoservice.BankBranchClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/bank-branch")
@RequiredArgsConstructor
@Tag(name = "Bank Branches Controller", description = "Getting data about exchange rates")
public class BankBranchesController {

    private final BankBranchClient bankBranchClient;

    @Operation(summary = "Getting bank branches", description = "Get information about  bank branches")
    @ApiResponse(responseCode = "200",
                 description = "Found the bank branches",
                 content = {@Content(mediaType = "application/json",
                         array = @ArraySchema(schema = @Schema(implementation = ResponseBankBranchDto.class)))})
    @GetMapping()
    public ResponseEntity<List<ResponseBankBranchDto>> getActualExchangeRates() {
        return bankBranchClient.getAllBankBranches();
    }

    @Operation(summary = "Getting filtered bank branches", description = "Get coordinates filtered of bank branches")
    @ApiResponse(responseCode = "200",
                 description = "Filter bank branches",
                 content = {@Content(mediaType = "application/json",
                         array = @ArraySchema(schema = @Schema(implementation = ResponseBranchCoordinatesDto.class)))})
    @GetMapping("/filters")
    public ResponseEntity<List<ResponseBranchCoordinatesDto>> getFilteredBankBranches(@RequestParam(required = false) BankBranchType bankBranchType,
                                                                                      @RequestParam(required = false) Boolean closed,
                                                                                      @RequestParam(required = false) Boolean workAtWeekends,
                                                                                      @RequestParam(required = false) Boolean cashWithdraw,
                                                                                      @RequestParam(required = false) Boolean moneyTransfer,
                                                                                      @RequestParam(required = false) Boolean acceptPayment,
                                                                                      @RequestParam(required = false) Boolean currencyExchange,
                                                                                      @RequestParam(required = false) Boolean exoticCurrency,
                                                                                      @RequestParam(required = false) Boolean ramp,
                                                                                      @RequestParam(required = false) Boolean replenishCard,
                                                                                      @RequestParam(required = false) Boolean replenishAccount,
                                                                                      @RequestParam(required = false) Boolean consultation,
                                                                                      @RequestParam(required = false) Boolean insurance,
                                                                                      @RequestParam(required = false) Boolean replenishWithoutCard) {
        return bankBranchClient.getFilteredBankBranches(
                bankBranchType,
                closed,
                workAtWeekends,
                cashWithdraw,
                moneyTransfer,
                acceptPayment,
                currencyExchange,
                exoticCurrency,
                ramp,
                replenishCard,
                replenishAccount,
                consultation,
                insurance,
                replenishWithoutCard);
    }
}
