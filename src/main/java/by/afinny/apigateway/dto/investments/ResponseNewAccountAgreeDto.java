package by.afinny.apigateway.dto.investments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO for response creation new account agree in the Investments Service")
public class ResponseNewAccountAgreeDto {

    @Schema(description = "agree date")
    private LocalDate agreeDate;
    @Schema(description = "first name")
    private String firstName;
    @Schema(description = "middle name")
    private String middleName;
    @Schema(description = "last name")
    private String lastName;
    @Schema(description = "phone number")
    private String phoneNumber;
    @Schema(description = "client id")
    private String clientId;
    @Schema(description = "email")
    private String email;
    @Schema(description = "passport number")
    private String passportNumber;
}
