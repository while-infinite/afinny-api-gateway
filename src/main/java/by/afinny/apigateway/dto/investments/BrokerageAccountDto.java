package by.afinny.apigateway.dto.investments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for a brokerageAccount")
public class BrokerageAccountDto {

    @Schema(description = "brokerage account id")
    private UUID brokerageAccountId;
    @Schema(description = "name account")
    private String nameAccount;
    @Schema(description = "brokerage account quantity")
    private Integer brokerageAccountQuantity;

}