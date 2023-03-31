package by.afinny.apigateway.dto.deposit;

import by.afinny.apigateway.dto.deposit.constant.CardStatus;
import by.afinny.apigateway.dto.deposit.constant.DigitalWallet;
import by.afinny.apigateway.dto.deposit.constant.PaymentSystem;
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
@Schema(description = "DTO for outgoing data of Cards")
public class CardDto {

    @Schema(description = "id")
    private UUID cardId;
    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "transaction limit")
    private BigDecimal transactionLimit;
    @Schema(description = "status")
    private CardStatus status;
    @Schema(description = "expiration date")
    private LocalDate expirationDate;
    @Schema(description = "holder name")
    private String holderName;
    @Schema(description = "digital wallet")
    private DigitalWallet digitalWallet;
    @Schema(description = "is default")
    private Boolean isDefault;
    @Schema(description = "card product id")
    private Integer cardProductId;
    @Schema(description = "card name")
    private String cardName;
    @Schema(description = "payment system")
    private PaymentSystem paymentSystem;
}