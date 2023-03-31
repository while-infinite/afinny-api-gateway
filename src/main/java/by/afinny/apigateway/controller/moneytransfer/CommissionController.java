package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CommissionDto;
import by.afinny.apigateway.dto.moneytransfer.constant.CurrencyCode;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import by.afinny.apigateway.openfeign.moneytransfer.CommissionClient;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/commission")
@Tag(name = "Commission Controller", description = "Manage actions with commission")
public class CommissionController {

    public final static String COMMISSION_URL = "/api/v1/commission";

    private final CommissionClient commissionClient;

    @Operation(summary = "Get commission", description = "Get information about commission")
    @ApiResponse(responseCode = "200", description = "Found the commission",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = CommissionDto.class))})
    @GetMapping()
    public ResponseEntity<CommissionDto> getCommissionData(@RequestParam TransferTypeName typeName,
                                              @RequestParam CurrencyCode currencyCode) {
        return commissionClient.getCommissionData(typeName, currencyCode);
    }
}
