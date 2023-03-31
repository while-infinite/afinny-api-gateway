package by.afinny.apigateway.dto.credit;

import by.afinny.apigateway.dto.credit.constant.CreditCardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PUBLIC)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for request changed credit card status")
public class CardStatusDto {

    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "card status")
    private CreditCardStatus cardStatus;
}
