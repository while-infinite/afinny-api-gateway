package by.afinny.apigateway.dto.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
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
@Schema(description = "DTO for viewing enabled auto payments")
public class AutoPaymentsDto {

    @Schema(description = "transfer order id")
    private UUID transferOrderId;
    @Schema(description = "type name")
    private TransferTypeName typeName;
}
