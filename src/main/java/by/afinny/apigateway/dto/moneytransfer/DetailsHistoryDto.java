package by.afinny.apigateway.dto.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.constant.PayeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for history details transfer order")
public class DetailsHistoryDto {

    @Schema(description = "created at")
    private LocalDateTime createdAt;
    @Schema(description = "remitter card number")
    private String remitterCardNumber;
    @Schema(description = "sum commission")
    private BigDecimal sumCommission;
    @Schema(description = "currency exchange")
    private BigDecimal currencyExchange;
    @Schema(description = "type")
    private PayeeType payeeType;
    @Schema(description = "name")
    private String name;
    @Schema(description = "INN")
    private String inn;
    @Schema(description = "BIC")
    private String bic;
    @Schema(description = "payee account number")
    private String payeeAccountNumber;
    @Schema(description = "payee card number")
    private String payeeCardNumber;
}