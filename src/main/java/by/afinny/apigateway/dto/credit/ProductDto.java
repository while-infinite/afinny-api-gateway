package by.afinny.apigateway.dto.credit;

import by.afinny.apigateway.dto.credit.constant.CalculationMode;
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
@Schema(description = "DTO outgoing data of a product")
public class ProductDto {

    @Schema(description = "product id")
    private Integer id;
    @Schema(description = "name")
    private String name;
    @Schema(description = "minimum summary")
    private BigDecimal minSum;
    @Schema(description = "maximum summary")
    private BigDecimal maxSum;
    @Schema(description = "currency code")
    private String currencyCode;
    @Schema(description = "minimum interest rate")
    private BigDecimal minInterestRate;
    @Schema(description = "maximum interest rate")
    private BigDecimal maxInterestRate;
    @Schema(description = "do need guarantees")
    private Boolean needGuarantees;
    @Schema(description = "is delivery in cash")
    private Boolean deliveryInCash;
    @Schema(description = "is early repayment")
    private Boolean earlyRepayment;
    @Schema(description = "do need income details")
    private Boolean needIncomeDetails;
    @Schema(description = "minimum months period")
    private Integer minPeriodMonths;
    @Schema(description = "maximum months period")
    private Integer maxPeriodMonths;
    @Schema(description = "is product active")
    private Boolean isActive;
    @Schema(description = "details")
    private String details;
    @Schema(description = "calculation mode")
    private CalculationMode calculationMode;
    @Schema(description = "grace months period")
    private Integer gracePeriodMonths;
    @Schema(description = "is rate adjustable")
    private Boolean rateIsAdjustable;
    @Schema(description = "base rate")
    private String rateBase;
    @Schema(description = "fix part rate")
    private BigDecimal rateFixPart;
    @Schema(description = "increased rate")
    private BigDecimal increasedRate;
    @Schema(description = "auto precessing")
    private Boolean autoProcessing;
}
