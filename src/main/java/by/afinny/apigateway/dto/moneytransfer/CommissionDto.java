package by.afinny.apigateway.dto.moneytransfer;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "DTO for outgoing data of Commission")
public class CommissionDto {

    @Schema(description = "id")
    private Integer id;
    @Schema(description = "min commission")
    private BigDecimal minCommission;
    @Schema(description = "max commission")
    private BigDecimal maxCommission;
    @Schema(description = "percent commission")
    private BigDecimal percentCommission;
    @Schema(description = "fix commission")
    private BigDecimal fixCommission;
    @Schema(description = "min sum")
    private BigDecimal minSum;
    @Schema(description = "max sum")
    private BigDecimal maxSum;
}
