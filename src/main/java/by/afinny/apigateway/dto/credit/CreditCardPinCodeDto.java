package by.afinny.apigateway.dto.credit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for change card pin code")
public class CreditCardPinCodeDto {

    @Schema(description = "credit card number")
    private String cardNumber;
    @Schema(description = "credit card new pin")
    private String newPin;
}
