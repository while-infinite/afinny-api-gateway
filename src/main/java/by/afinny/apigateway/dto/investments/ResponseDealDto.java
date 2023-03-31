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
import java.time.LocalDate;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for response operation deal details")
public class ResponseDealDto {

    @Schema(description = "id deal")
    private UUID id;
    @Schema(description = "asset id")
    private UUID assetId;
    @Schema(description = "id operation")
    private String dealType;
    @Schema(description = "purchase price")
    private BigDecimal purchasePrice;
    @Schema(description = "selling price")
    private BigDecimal sellingPrice;
    @Schema(description = "sum")
    private BigDecimal sum;
    @Schema(description = "date deal")
    private LocalDate dateDeal;
    @Schema(description = "commission")
    private BigDecimal commission;
}
