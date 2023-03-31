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

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NewPolicyFromUserDto {

    private String periodOfInsurance;
    private String paymentCycle;
    private BigDecimal policySum;
    private String insuranceStatus;
    private LocalDate registrationDate;
    private LocalDate startDate;
    private String insuranceType;
    private String region;
    private Boolean isWithInsuredAccident;
    private String categoryGroup;
    private String capacityGroup;
    private LocalDate birthday;
    private String drivingExperience;
    private String model;
    private String carNumber;
    private String factorName;
}
