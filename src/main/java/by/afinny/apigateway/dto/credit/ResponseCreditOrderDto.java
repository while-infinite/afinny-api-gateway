package by.afinny.apigateway.dto.credit;

import by.afinny.apigateway.dto.credit.constant.CreditOrderStatus;
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
@Schema(description = "DTO for response created credit order")
public class ResponseCreditOrderDto {

    @Schema(description = "credit order id")
    private UUID id;
    @Schema(description = "product id")
    private Integer productId;
    @Schema(description = "product name")
    private String productName;
    @Schema(description = "credit order status")
    private CreditOrderStatus status;
    @Schema(description = "amount")
    private BigDecimal amount;
    @Schema(description = "months period")
    private Integer periodMonths;
    @Schema(description = "creation date")
    private LocalDate creationDate;
    @Schema(description = "principal")
    private BigDecimal principal;
    @Schema(description = "interest")
    private BigDecimal interest;
    @Schema(description = "principal debt")
    private BigDecimal principalDebt;
    @Schema(description = "interest debt")
    private BigDecimal interestDebt;
    @Schema(description = "payment date")
    private LocalDate paymentDate;
}
