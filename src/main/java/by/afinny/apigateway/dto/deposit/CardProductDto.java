package by.afinny.apigateway.dto.deposit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO outgoing data of a card product")
public class CardProductDto {

    @Schema(description = "Card product id")
    private Integer id;
    @Schema(description = "Card product name")
    private String cardName;
    @Schema(description = "Card product payment system")
    private String paymentSystem;
    @Schema(description = "Cashback size")
    private BigDecimal cashback;
    @Schema(description = "Name of the co-branding company")
    private String coBrand;
    @Schema(description = "Is it virtual")
    private Boolean isVirtual;
    @Schema(description = "Premium status")
    private String premiumStatus;
    @Schema(description = "monthly maintenance cost")
    private BigDecimal servicePrice;
    @Schema(description = "Purchase price")
    private BigDecimal productPrice;
    @Schema(description = "Currency code")
    private String currencyCode;
    @Schema(description = "Is there an opportunity to order a product")
    private Boolean isActive;
    @Schema(description = "Validity period of the selected card product")
    private Integer cardDuration;
}
