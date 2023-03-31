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

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProgramDto {
    @Schema(name = "is emergency hospitalization")
    Boolean isEmergencyHospitalization;
    @Schema(name = "is dental service")
    Boolean isDentalService;
    @Schema(name = "is telemedicine")
    Boolean isTelemedicine;
    @Schema(name = "is emergency medical care")
    Boolean isEmergencyMedicalCare;
    @Schema(name = "is calling doctor")
    Boolean isCallingDoctor;
    @Schema(name = "is outpatient service")
    Boolean isOutpatientService;
    @Schema(name = "name")
    String name;
    @Schema(name = "organization")
    String organization;
    @Schema(name = "link")
    String link;
    @Schema(name = "sum")
    BigDecimal sum;
    @Schema(name = "description")
    String description;
    @Schema(name = "program id")
    String programId;
}
