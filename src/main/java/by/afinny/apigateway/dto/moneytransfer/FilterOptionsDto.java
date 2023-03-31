package by.afinny.apigateway.dto.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.constant.OperationType;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class FilterOptionsDto {

    @Schema(description = "client id")
    private UUID clientId;
    @Schema(description = "page number")
    @NotNull
    private Integer pageNumber;
    @Schema(description = "page size")
    @NotNull
    private Integer pageSize;
    @Schema(description = "purpose")
    private String purpose;
    @Schema(description = "sum")
    private BigDecimal sum;
    @Schema(description = "min sum")
    private BigDecimal min_sum;
    @Schema(description = "max sum")
    private BigDecimal max_sum;
    @Schema(description = "from")
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from;
    @Schema(description = "to")
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to;
    @Schema(description = "remitter card number")
    private String remitterCardNumber;
    @Schema(description = "transfer type name")
    private TransferTypeName type_name;
    @Schema(description = "transfer operation type")
    private OperationType operationType;
}
