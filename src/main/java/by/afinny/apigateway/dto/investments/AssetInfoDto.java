package by.afinny.apigateway.dto.investments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetInfoDto {

    @Schema(description = "asset id")
    private String assetId;
    @Schema(description = "asset amount")
    private int amount;
    @Schema(description = "asset price")
    private String last;
    @Schema(description = "changing price in rubles")
    private String changingPriceAssetRubles;
    @Schema(description = "changing price in percent")
    private String changingPriceAssetPercent;
    @Schema(description = "asset type")
    private String assetType;
    @Schema(description = "asset id")
    private String secId;
    @Schema(description = "asset name")
    private String name;
    @Schema(description = "asset description")
    private String description;
}
