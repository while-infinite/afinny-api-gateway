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
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for outgoing data of Cards")
public class CardDebitLimitDto {

    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "transaction limit")
    private BigDecimal transactionLimit;
}