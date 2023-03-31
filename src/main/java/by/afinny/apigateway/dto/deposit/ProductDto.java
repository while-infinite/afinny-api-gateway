package by.afinny.apigateway.dto.deposit;

import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO outgoing data of a deposit product")
public class ProductDto {

    @Schema(description = "Product name")
    private String name;
    @Schema(description = "Product id")
    private Integer id;
    @Schema(description = "Minimal interest rate")
    private BigDecimal minInterestRate;
    @Schema(description = "Maximum interest rate")
    private BigDecimal maxInterestRate;
    @Schema(description = "Interest rate early")
    private BigDecimal interestRateEarly;
    @Schema(description = "Currency code")
    private CurrencyCode currencyCode;
    @Schema(description = "Is revocable")
    private Boolean isRevocable;
    @Schema(description = "Schema name")
    private String schemaName;
    @Schema(description = "Is capitalization")
    private Boolean isCapitalization;
    @Schema(description = "Minimum duration months")
    private Integer minDurationMonths;
    @Schema(description = "Maximum duration months")
    private Integer maxDurationMonths;
    @Schema(description = "Minimum amount")
    private BigDecimal amountMin;
    @Schema(description = "Maximum amount")
    private BigDecimal amountMax;
}