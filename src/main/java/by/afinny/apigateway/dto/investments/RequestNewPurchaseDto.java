package by.afinny.apigateway.dto.investments;

import by.afinny.apigateway.dto.investments.constant.AssetType;
import by.afinny.apigateway.dto.investments.constant.DealType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "DTO for request new Purchase")
public class RequestNewPurchaseDto {
    private UUID idBrokerageAccount;
    private UUID idAsset;
    private Integer amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal BID;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal LAST;
    private AssetType assetType;
    private DealType dealType;
    private LocalDate dateDeal;
}
