package by.afinny.apigateway.dto.deposit;

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
@Schema(description = "DTO for change pin-code debit card")
public class NewPinCodeDebitCardDto {

    @Schema(description = "cardNumber")
    String cardNumber;
    @Schema(description = "newPin")
    String newPin;
}
