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
@Schema(description = "DTO for request new credit order")
public class RequestCreditOrderDto {

    @Schema(description = "product id")
    private Integer productId;
    @Schema(description = "amount")
    private BigDecimal amount;
    @Schema(description = "months period")
    private Integer periodMonths;
    @Schema(description = "creation date")
    private LocalDate creationDate;
    @Schema(description = "monthly income")
    private BigDecimal monthlyIncome;
    @Schema(description = "monthly expenditure")
    private BigDecimal monthlyExpenditure;
    @Schema(description = "employer identification number")
    private String employerIdentificationNumber;
}
