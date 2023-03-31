package by.afinny.apigateway.dto.insurance;

import by.afinny.apigateway.dto.insurance.constant.InsuranceType;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO for response payment details")
public class ResponsePaymentDetailsDto {

    @Schema(description = "policy sum")
    private BigDecimal policySum;
    @Schema(description = "insurance type")
    private InsuranceType insuranceType;
}
