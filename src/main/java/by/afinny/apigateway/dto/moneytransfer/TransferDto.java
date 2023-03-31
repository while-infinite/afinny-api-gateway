package by.afinny.apigateway.dto.moneytransfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for outgoing data of Transfer Order")
public class TransferDto {

    @Schema(description = "transfer type id")
    private Integer transferTypeId;
    @Schema(description = "purpose")
    private String purpose;
    @Schema(description = "payee id")
    private UUID payeeId;
    @Schema(description = "sum")
    private BigDecimal sum;
    @Schema(description = "is favorite")
    private Boolean isFavorite;
}
