package by.afinny.apigateway.dto.insurance;

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
@Schema(description = "DTO for response sum assignment")
public class ResponseSumAssignmentDto {

    @Schema(description = "id")
    private String id;
    @Schema(description = "name")
    private String name;
    @Schema(description = "min sum")
    private String minSum;
    @Schema(description = "max sum")
    private String maxSum;
    @Schema(description = "default sum")
    private String defaultSum;
}
