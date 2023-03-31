package by.afinny.apigateway.dto.deposit;

import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
import by.afinny.apigateway.dto.deposit.constant.SchemaName;
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

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "DTO for outgoing data of Deposits")
public class DepositDto {

    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "start date")
    private LocalDateTime startDate;
    @Schema(description = "end date")
    private LocalDateTime endDate;
    @Schema(description = "interest rate")
    private BigDecimal interestRate;
    @Schema(description = "current balance")
    private BigDecimal currentBalance;
    @Schema(description = "auto renewal")
    private Boolean autoRenewal;
    @Schema(description = "name")
    private String name;
    @Schema(description = "currency code")
    private CurrencyCode currencyCode;
    @Schema(description = "schema name")
    private SchemaName schemaName;
    @Schema(description = "is capitalization")
    private Boolean isCapitalization;
    @Schema(description = "is revocable")
    private Boolean isRevocable;
}
