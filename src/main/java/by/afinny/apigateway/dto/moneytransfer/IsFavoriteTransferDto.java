package by.afinny.apigateway.dto.moneytransfer;

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
@Schema(description = "DTO to receive a selected favorite transferOrder")
public class IsFavoriteTransferDto {

    @Schema(name = "is_favorite")
    private Boolean isFavorite;
}
