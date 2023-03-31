package by.afinny.apigateway.dto.credit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for response operation credit")
public class ResponseOperationDto {

    @Schema(description = "id operation")
    private UUID operationId;
    @Schema(description = "date of transaction processing by the bank")
    private LocalDateTime completedAt;
    @Schema(description = "purpose of the operation")
    private String details;
    @Schema(description = "id account")
    private UUID accountId;
    @Schema(description = "sum operation")
    private BigDecimal sum;
    @Schema(description = "type operation")
    private String operationType;
    @Schema(description = "currency code")
    private String currencyCode;
    @Schema(description = "type credit")
    private String type;
}
