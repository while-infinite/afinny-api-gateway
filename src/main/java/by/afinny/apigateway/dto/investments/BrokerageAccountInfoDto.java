package by.afinny.apigateway.dto.investments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrokerageAccountInfoDto {

    @Schema(description = "brokerage account id")
    private String brokerageAccountId;
    @Schema(description = "account name")
    private String nameAccount;
    @Schema(description = "brokerage account quantity")
    private String brokerageAccountQuantity;
    @Schema(description = "changing quantity in rubles")
    private String changingQuantityRubles;
    @Schema(description = "changing quantity in percent")
    private String changingQuantityPercent;
    @Schema(description = "rubles amount")
    private String rubles;
    @Schema(description = "list of asset info")
    private List<AssetInfoDto> assetInfoDtoList;
}