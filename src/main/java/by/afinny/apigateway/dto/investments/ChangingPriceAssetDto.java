package by.afinny.apigateway.dto.investments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO with changing prices by user deals")
public class ChangingPriceAssetDto {
    private String secId;
    private String boardId;
    private String changingPriceAssetRubles;
    private String changingPriceAssetPercent;
}
