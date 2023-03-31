package by.afinny.apigateway.dto.credit;

import by.afinny.apigateway.dto.credit.constant.CreditCardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "DTO for request card status")
public class RequestCardStatusDto {

    @Schema(description = "card number")
    private String cardNumber;
    @Schema(description = "card status")
    private CreditCardStatus cardStatus;
}
