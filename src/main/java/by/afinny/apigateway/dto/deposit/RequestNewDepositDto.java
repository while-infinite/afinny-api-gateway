package by.afinny.apigateway.dto.deposit;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "DTO to create agreement")
public class RequestNewDepositDto {

    @Schema(description = "product id")
    private Integer productId;
    @Schema(description = "initial amount")
    private BigDecimal initialAmount;
    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "auto renewal")
    private Boolean autoRenewal;
    @Schema(description = "interest rate")
    private BigDecimal interestRate;
    @Schema(description = "duration month")
    private Integer durationMonth;
}
