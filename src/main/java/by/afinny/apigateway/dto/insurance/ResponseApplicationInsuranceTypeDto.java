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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for response insurance types")
public class ResponseApplicationInsuranceTypeDto {
    @Schema(description = "Insurance types")
    private InsuranceType insuranceType;
}
