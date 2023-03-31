package by.afinny.apigateway.dto.credit;

import by.afinny.apigateway.dto.credit.constant.CreditCardStatus;
import by.afinny.apigateway.dto.credit.constant.PaymentSystem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO outgoing data of a information about credit card")
public class CardInfoDto {

    @Schema(description = "credit id")
    private String creditId;
    @Schema(description = "account number")
    private String accountNumber;
    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "card balance")
    private BigDecimal balance;
    @Schema(description = "holder name")
    private String holderName;
    @Schema(description = "expiration date")
    private String expirationDate;
    @Schema(description = "payment system")
    private PaymentSystem paymentSystem;
    @Schema(description = "card status")
    private CreditCardStatus status;
    @Schema(description = "transaction lLimit")
    private BigDecimal transactionLimit;
    @Schema(description = "product name")
    private String name;
    @Schema(description = "principal debt")
    private BigDecimal principalDebt;
    @Schema(description = "credit limit")
    private BigDecimal creditLimit;
    @Schema(description = "credit currency code")
    private String creditCurrencyCode;
    @Schema(description = "termination date")
    private String terminationDate;
}
