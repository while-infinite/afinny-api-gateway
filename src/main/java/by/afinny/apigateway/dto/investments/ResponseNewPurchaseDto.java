package by.afinny.apigateway.dto.investments;

import by.afinny.apigateway.dto.investments.constant.AssetType;
import by.afinny.apigateway.dto.investments.constant.DealType;
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
@Schema(description = "DTO for response created new Purchase")
public class ResponseNewPurchaseDto {
    @Schema(description = "asset id")
    private UUID asset_id;
    @Schema(description = "purchase amount")
    private Integer amount;
    @Schema(description = "purchase price")
    private BigDecimal purchase_price;
    @Schema(description = "asset type")
    private AssetType asset_type;
    @Schema(description = "deal type")
    private DealType dealType;
    @Schema(description = "date deal")
    private LocalDate dateDeal;

}
