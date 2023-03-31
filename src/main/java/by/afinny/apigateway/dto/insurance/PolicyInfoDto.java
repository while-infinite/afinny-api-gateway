package by.afinny.apigateway.dto.insurance;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(description = "DTO outgoing data of a information about policy")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyInfoDto {
    private String insuranceStatus;
    private String number;
    private LocalDate agreementDate;
    private LocalDate startDate;
    private String periodOfInsurance;
    private String paymentCycle;
    private BigDecimal insuranceSum;
    private BigDecimal policySum;
    private String region;
    private String district;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String model;
    private String carNumber;
    private String clientId;
    private Boolean isFlat;
    private String firstName;
    private String middleName;
    private String lastName;
}
