package by.afinny.apigateway.dto.credit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO outgoing data of a credit's payment schedule")
public class CreditScheduleDto {

    @Schema(description = "Account number")
    private String accountNumber;
    @Schema(description = "Agreement id")
    private UUID agreementID;
    @Schema(description = "Principal debt")
    private BigDecimal principalDebt;
    @Schema(description = "Interest debt")
    private BigDecimal interestDebt;
    @Schema(implementation = PaymentScheduleDto.class)
    @JsonProperty("payments")
    private List<PaymentScheduleDto> paymentsSchedule;
}