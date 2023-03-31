package by.afinny.apigateway.dto.infoservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(name = "DTO for response currency exchange rate")
public class CurrencyExchangeRateDto {
    @Schema(name = "currencyRate")
    private String currencyRate;
}
