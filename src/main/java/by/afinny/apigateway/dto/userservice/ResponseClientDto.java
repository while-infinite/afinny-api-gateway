package by.afinny.apigateway.dto.userservice;

import by.afinny.apigateway.dto.userservice.constant.ClientStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "DTO outgoing data of a registered client")
public class ResponseClientDto {

    @Schema(description = "unique client identifier")
    private UUID id;
    @Schema(description = "sms notification")
    private boolean smsNotification;
    @Schema(description = "push notification")
    private boolean pushNotification;
    @Schema(description = "client's mobile phone")
    private String mobilePhone;
    @Schema(description = "client's password")
    private String password;
    @Schema(description = "client's first name")
    private String firstName;
    @Schema(description = "client's middle name")
    private String middleName;
    @Schema(description = "client's last name")
    private String lastName;
    @Schema(description = "client's identification passport number")
    private String passportNumber;
    @Schema(description = "client's security question")
    private String securityQuestion;
    @Schema(description = "client's security answer")
    private String securityAnswer;
    @Schema(description = "client's email")
    private String email;
    @Schema(description = "client's status")
    private ClientStatus clientStatus;
    @Schema(description = "client's country of residence")
    private Boolean countryOfResidence;
    @Schema(description = "date of registration of the client in the application")
    private LocalDate appRegistrationDate;
    @Schema(description = "date accession")
    private LocalDate accessionDate;
}
