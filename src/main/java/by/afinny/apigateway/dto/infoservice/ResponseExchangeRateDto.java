package by.afinny.apigateway.dto.infoservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter(AccessLevel.PUBLIC)
@Schema(description = "DTO for response actual exchange rate")
@JsonPropertyOrder({
        "SECID",
        "BOARDID",
        "buyingRate",
        "sellingRate"
})
public class ResponseExchangeRateDto {
    @JsonProperty("SECID")
    public String secid;
    @JsonProperty("BOARDID")
    public String boardid;
    public double buyingRate;
    public double sellingRate;
    public String currencyCode;
    public String name;

    @Getter
    @Setter(AccessLevel.PUBLIC)
    @ToString
    public static class ResponseExchangeRateList {

        @Schema(name = "marketdata")
        @JsonProperty("marketdata")
        private List<ResponseExchangeRateDto> exchangeRates;

    }
}
