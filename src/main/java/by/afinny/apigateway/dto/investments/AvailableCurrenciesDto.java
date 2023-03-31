package by.afinny.apigateway.dto.investments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "SECID",
        "BOARDID",
        "LAST",
        "CHANGE",
        "LASTTOPREVPRICE",
        "DECIMALS",
        "FACEVALUE",
        "MARKETCODE"
})
@Schema(description = "DTO for available currencies from Moscow Exchange API")
public class AvailableCurrenciesDto implements Serializable {

    @JsonProperty("SECID")
    public String secid;
    @JsonProperty("BOARDID")
    public String boardid;
    @JsonProperty("LAST")
    public double last;
    @JsonProperty("CHANGE")
    public double change;
    @JsonProperty("LASTTOPREVPRICE")
    public double lasttoprevprice;

    @Getter
    @Setter(AccessLevel.PUBLIC)
    @ToString
    public static class AllAvailableCurrencies implements Serializable {

        @Schema(name = "marketdata")
        @JsonProperty("marketdata")
        private List<AvailableCurrenciesDto> currencies;

    }

}
