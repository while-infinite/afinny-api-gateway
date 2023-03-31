package by.afinny.apigateway.dto.deposit;

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
@Builder
@ToString
@Schema(description = "DTO incoming data of a deposit")
public class WithdrawDepositDto {

    @Schema(description = "card number")
    private String cardNumber;
}
