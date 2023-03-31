package by.afinny.apigateway.dto.credit;

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
import java.time.LocalDate;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for a credit card")
public class CreditCardDto {

    @Schema(description = "Credit card's id")
    private UUID id;
    @Schema(description = "Credit card's account number")
    private String accountNumber;
    @Schema(description = "Credit card's number")
    private String cardNumber;
    @Schema(description = "Credit card's balance")
    private BigDecimal balance;
    @Schema(description = "Currency code")
    private String currencyCode;
    @Schema(description = "Payment system")
    private PaymentSystem paymentSystem;
    @Schema(description = "Expiration date")
    private LocalDate expirationDate;
    @Schema(description = "Product name")
    private String name;
}