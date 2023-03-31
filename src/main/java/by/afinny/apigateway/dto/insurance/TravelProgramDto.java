package by.afinny.apigateway.dto.insurance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TravelProgramDto {
    @Schema(name = "travel program name")
    String name;
    @Schema(name = "travel program description")
    String description;
    @Schema(name = "final program price")
    BigDecimal finalPrice;
    @Schema(name = "maximal insurance payment")
    BigDecimal maxInsuranceSum;
    @Schema(name = "travel program id")
    String travelProgramId;
}
