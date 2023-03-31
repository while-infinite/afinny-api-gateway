package by.afinny.apigateway.dto.insurance;

import by.afinny.apigateway.dto.insurance.constant.InsuranceCountry;
import by.afinny.apigateway.dto.insurance.constant.InsuranceStatus;
import by.afinny.apigateway.dto.insurance.constant.InsuranceType;
import by.afinny.apigateway.dto.insurance.constant.SportType;
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
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for request of new travel policy")
public class RequestTravelPolicyDto {
    private InsuranceCountry insuranceCountry;
    private LocalDate birthday;
    private SportType sportType;
    private BigDecimal insuranceSum;
    private BigDecimal policySum;
    private LocalDate startDate;
    private LocalDate lastDate;
    private String firstName;
    private String middleName;
    private String lastName;
    private String passportNumber;
    private String email;
    private String phoneNumber;
    private InsuranceType insuranceType;
    private LocalDate registrationDate;
    private InsuranceStatus insuranceStatus;
    private String clientId;
    private UUID travelProgramId;
}
