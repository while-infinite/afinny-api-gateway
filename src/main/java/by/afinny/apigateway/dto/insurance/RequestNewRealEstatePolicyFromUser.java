package by.afinny.apigateway.dto.insurance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO of registration of real estate policy from User")
public class RequestNewRealEstatePolicyFromUser {

    private String location;
    private BigDecimal insuranceSum;
    private Boolean isFlat;
    private String paymentCycle;
    private String periodOfInsurance;
    private LocalDate registrationDate;
    private String sumAssignmentName;
    private BigDecimal policySum;
    private String insuranceType;
    private BigDecimal sumAssignmentSum;
    private BigDecimal sumAssignmentGeneralSum;
    private BigDecimal sumAssignmentMinSum;
    private BigDecimal sumAssignmentMaxSum;
    private BigDecimal sumAssignmentDefaultSum;

}
