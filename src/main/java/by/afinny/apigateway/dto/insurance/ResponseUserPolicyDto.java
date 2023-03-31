package by.afinny.apigateway.dto.insurance;

import by.afinny.apigateway.dto.insurance.constant.InsuranceStatus;
import by.afinny.apigateway.dto.insurance.constant.InsuranceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO for response user policy")
public class ResponseUserPolicyDto {

    @Schema(description = "application id")
    private UUID applicationId;
    @Schema(description = "insurance type")
    private InsuranceType insuranceType;
    @Schema(description = "agreement number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String number;
    @Schema(description = "agreement date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate agreementDate;
    @Schema(description = "registration date")
    private LocalDate registrationDate;
    @Schema(description = "insurance status")
    private InsuranceStatus insuranceStatus;

}
