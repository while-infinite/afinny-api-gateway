package by.afinny.apigateway.dto.deposit;

import by.afinny.apigateway.dto.deposit.constant.PaymentSystem;
import by.afinny.apigateway.dto.infoservice.constant.CurrencyCode;
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
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for outgoing data of Account")
public class AccountWithCardInfoDto {

    @Schema(description = "card id")
    private UUID cardId;
    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "expiration date")
    private LocalDate expirationDate;
    @Schema(description = "card name")
    private String cardName;
    @Schema(description = "payment system")
    private PaymentSystem paymentSystem;
    @Schema(description = "currency code")
    private CurrencyCode currencyCode;
    @Schema(description = "card balance")
    private BigDecimal cardBalance;
}