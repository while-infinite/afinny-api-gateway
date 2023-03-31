package by.afinny.apigateway.dto.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.constant.CurrencyCode;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferStatus;
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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for outgoing data of Transfer Order History")
public class TransferOrderHistoryDto {

    @Schema(name = "transfer order id")
    private UUID transferOrderId;
    @Schema(name = "create date")
    private LocalDateTime createdAt;
    @Schema(description = "purpose")
    private String purpose;
    @Schema(description = "payee Id")
    private UUID payeeId;
    @Schema(description = "name")
    private String name;
    @Schema(description = "sum")
    private BigDecimal sum;
    @Schema(description = "complete date")
    private LocalDateTime completedAt;
    @Schema(description = "transfer status")
    private TransferStatus transferStatus;
    @Schema(description = "transfer type Name")
    private TransferTypeName transferTypeName;
    @Schema(description = "currency code")
    private CurrencyCode currencyCode;
    @Schema(description = "remitter card number")
    private String remitterCardNumber;
}

