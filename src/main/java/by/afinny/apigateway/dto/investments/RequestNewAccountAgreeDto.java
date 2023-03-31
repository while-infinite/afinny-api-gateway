package by.afinny.apigateway.dto.investments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO of creation new account agree in the Investments Service")
public class RequestNewAccountAgreeDto {
    private String firstName;

    private String middleName;

    private String lastName;

    private String phoneNumber;

    private String clientId;

    private String email;

    private String passportNumber;
}
