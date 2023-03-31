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
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for a credit")
public class CreditDto {

    @Schema(description = "credit id")
    private UUID creditId;
    @Schema(description = "name")
    private String name;
    @Schema(description = "principal debt")
    private BigDecimal principalDebt;
    @Schema(description = "credit limit")
    private BigDecimal creditLimit;
    @Schema(description = "credit currency code")
    private String creditCurrencyCode;
    @Schema(description = "termination date")
    private LocalDate terminationDate;
}
