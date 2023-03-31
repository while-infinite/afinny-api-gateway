package by.afinny.apigateway.dto.deposit;

import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
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
@Schema(description = "DTO outgoing data of a deposit product")
public class ActiveDepositDto {

    @Schema(description = "Agreement id")
    private UUID agreementId;
    @Schema(description = "Start date")
    private LocalDateTime startDate;
    @Schema(description = "End date")
    private LocalDateTime endDate;
    @Schema(description = "Current balance")
    private BigDecimal currentBalance;
    @Schema(description = "Product name")
    private String productName;
    @Schema(description = "Currency code")
    private CurrencyCode currencyCode;
    @Schema(description = "Card number")
    private String cardNumber;
}
