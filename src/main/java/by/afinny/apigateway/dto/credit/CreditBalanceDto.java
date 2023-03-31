package by.afinny.apigateway.dto.credit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO outgoing data of a credit")
public class CreditBalanceDto {

    @Schema(description = "name")
    private String name;
    @Schema(description = "credit currency code")
    private String creditCurrencyCode;
    @Schema(description = "agreement date")
    private LocalDate agreementDate;
    @Schema(description = "agreement id")
    private UUID agreementId;
    @Schema(description = "agreement number")
    private String agreementNumber;
    @Schema(description = "account number")
    private String accountNumber;
    @Schema(description = "card")
    private CreditCardBalanceDto card;
    @Schema(description = "account currency code")
    private String accountCurrencyCode;
    @Schema(description = "credit limit")
    private BigDecimal creditLimit;
    @Schema(description = "interest rate")
    private BigDecimal interestRate;
    @Schema(description = "principal debt")
    private BigDecimal principalDebt;
    @Schema(description = "interest debt")
    private BigDecimal interestDebt;
    @Schema(description = "The nearest payment date")
    private LocalDate paymentDate;
    @Schema(description = "The nearest payment on the principal debt by date")
    private BigDecimal paymentPrincipal;
    @Schema(description = "The nearest interest payment by date")
    private BigDecimal paymentInterest;
}
