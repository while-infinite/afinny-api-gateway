package by.afinny.apigateway.dto.insurance;

import by.afinny.apigateway.dto.insurance.constant.DocumentType;
import by.afinny.apigateway.dto.insurance.constant.InsuranceStatus;
import by.afinny.apigateway.dto.insurance.constant.InsuranceType;
import by.afinny.apigateway.dto.insurance.constant.Period;
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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO for request of new medicine policy")
public class RequestMedicinePolicyDto {
    @Schema(description = "Birthday")
    private LocalDate birthday;
    @Schema(description = "Region")
    private String region;
    @Schema(description = "Document Number")
    private String documentNumber;
    @Schema(description = "Document Type")
    private DocumentType documentType;
    @Schema(description = "Insurance Sum")
    private BigDecimal insuranceSum;
    @Schema(description = "Policy Sum")
    private BigDecimal policySum;
    @Schema(description = "First Name")
    private String firstName;
    @Schema(description = "Middle Name")
    private String middleName;
    @Schema(description = "Last Name")
    private String lastName;
    @Schema(description = "Passport Number")
    private String passportNumber;
    @Schema(description = "Email")
    private String email;
    @Schema(description = "Phone Number")
    private String phoneNumber;
    @Schema(description = "Program Id")
    private Integer programId;
    @Schema(description = "Insurance Type")
    private InsuranceType insuranceType;
    @Schema(description = "Registration Date")
    private LocalDate registrationDate;
    @Schema(description = "Period Of Insurance")
    private Period periodOfInsurance;
    @Schema(description = "Period Of Insurance")
    private Period paymentCycle;
    @Schema(description = "Client Id")
    private String clientId;
}