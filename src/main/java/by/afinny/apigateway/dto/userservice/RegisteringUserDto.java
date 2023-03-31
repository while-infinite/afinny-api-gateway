package by.afinny.apigateway.dto.userservice;

import by.afinny.apigateway.dto.userservice.constant.ClientStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for request user registration")
public class RegisteringUserDto {

    @Schema(description = "client's id")
    private UUID id;
    @Schema(description = "mobile phone number")
    private String mobilePhone;
    @Schema(description = "client status")
    private ClientStatus clientStatus;
    @Schema(description = "password")
    private String password;
    @Schema(description = "security question")
    private String securityQuestion;
    @Schema(description = "security answer")
    private String securityAnswer;
    @Schema(description = "email")
    private String email;
}
