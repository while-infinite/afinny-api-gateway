package by.afinny.apigateway.dto.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.constant.CurrencyCode;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "DTO for outgoing data of debit card statement")
public class DebitCardStatementDto {

    @Schema(name = "transfer order id")
    private UUID transferOrderId;
    @Schema(name = "purpose")
    private String purpose;
    @Schema(name = "payee id")
    private UUID payeeId;
    @Schema(name = "sum")
    private BigDecimal sum;
    @Schema(name = "completed at")
    private LocalDateTime completedAt;
    @Schema(name = "type name")
    private TransferTypeName typeName;
    @Schema(name = "currency code")
    private CurrencyCode currencyCode;
}
