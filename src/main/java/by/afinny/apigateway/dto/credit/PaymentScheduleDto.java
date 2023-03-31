package by.afinny.apigateway.dto.credit;

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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO outgoing data of a payment schedule")
public class PaymentScheduleDto {

    @Schema(description = "Payment date")
    private LocalDate paymentDate;
    @Schema(description = "Payment on the principal debt")
    private BigDecimal paymentPrincipal;
    @Schema(description = "Interest payment")
    private BigDecimal paymentInterest;
}
