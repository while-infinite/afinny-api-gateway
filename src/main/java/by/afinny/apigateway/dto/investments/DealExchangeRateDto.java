package by.afinny.apigateway.dto.investments;


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
@Schema(description = "DTO for deals by clients brokerage account")
public class DealExchangeRateDto {
    private String secId;
    private String boardId;
    private Integer amount;
    private BigDecimal purchasePrice;
}
