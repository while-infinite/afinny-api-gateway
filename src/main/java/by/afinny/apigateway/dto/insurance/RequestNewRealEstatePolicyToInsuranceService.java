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
@Schema(description = "DTO of registration of real estate policy to Insurance Service")
public class RequestNewRealEstatePolicyToInsuranceService {

    private String city;
    private String clientId;
    private String district;
    private String email;
    private String firstName;
    private String flatNumber;
    private String houseNumber;
    private BigDecimal insuranceSum;
    private Boolean isFlat;
    private String lastName;
    private String middleName;
    private String paymentCycle;
    private String periodOfInsurance;
    private String phoneNumber;
    private String region;
    private LocalDate registrationDate;
    private String street;
    private String sumAssignmentName;
    private BigDecimal sumAssignmentSum;
    private BigDecimal policySum;
    private String insuranceType;
    private BigDecimal sumAssignmentGeneralSum;
    private BigDecimal sumAssignmentMinSum;
    private BigDecimal sumAssignmentMaxSum;
    private BigDecimal sumAssignmentDefaultSum;
}
