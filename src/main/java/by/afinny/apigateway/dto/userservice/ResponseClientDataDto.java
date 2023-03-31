package by.afinny.apigateway.dto.userservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO outgoing data about client")
public class ResponseClientDataDto {

    @Schema(description = "first name")
    private String firstName;
    @Schema(description = "last name")
    private String lastName;
    @Schema(description = "middle name")
    private String middleName;
    @Schema(description = "mobile phone")
    private String mobilePhone;
    @Schema(description = "email")
    private String email;
    @Schema(description = "passport_number")
    private String passportNumber;
    @Schema(description = "clientId")
    private String clientId;
    @Schema(description = "client_status")
    private String clientStatus;
    @Schema(description = "country of residence")
    private Boolean countryOfResidence;
}
