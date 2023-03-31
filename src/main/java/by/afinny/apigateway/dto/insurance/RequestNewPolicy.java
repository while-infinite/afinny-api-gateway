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
@Schema(description = "DTO of registration of a car insurance policy ")
public class RequestNewPolicy {

    private String periodOfInsurance;
    private String paymentCycle;
    private LocalDate registrationDate;
    private BigDecimal policySum;
    private String insuranceStatus;
    private LocalDate startDate;
    private String insuranceType;
    private String region;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String clientId;
    private String email;
    private Boolean isWithInsuredAccident;
    private String categoryGroup;
    private String capacityGroup;
    private LocalDate birthday;
    private String passportNumber;
    private String drivingExperience;
    private String model;
    private String carNumber;
    private String factorName;
}
