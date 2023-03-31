package by.afinny.apigateway.dto.insurance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class SuggestionDto {

    @Schema(name = "region")
    private String region;
    @Schema(name = "area")
    private String area;
    @Schema(name = "city")
    private String city;
    @Schema(name = "settlement with type")
    private String settlementWithType;
    @Schema(name = "street with type")
    private String streetWithType;
    @Schema(name = "house")
    private String house;
    @Schema(name = "block")
    private String block;
    @Schema(name = "flat")
    private String flat;

    @Getter
    @Setter(AccessLevel.PUBLIC)
    @ToString
    public static class Suggestions {

        @Schema(name = "suggestions")
        private List<SuggestionDto> suggestions;

    }

}
