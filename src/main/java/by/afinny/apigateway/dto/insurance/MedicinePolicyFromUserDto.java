package by.afinny.apigateway.dto.insurance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class MedicinePolicyFromUserDto {

    private String periodOfInsurance;
    private String paymentCycle;
    private LocalDate registrationDate;
    private BigDecimal insuranceSum;
    private BigDecimal policySum;
    private String insuranceType;
    private String region;
    private LocalDate birthday;
    private Integer programId;
    private String documentNumber;
    private String documentType;
}
