package by.afinny.apigateway.dto.insurance;

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
@Schema(description = "DTO for response factor")
public class FactorDto {

    @Schema(description = "factor name")
    private String factorName;
    @Schema(description = "factor rate")
    private Double factorRate;
}
