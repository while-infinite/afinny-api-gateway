package by.afinny.apigateway.dto.userservice;

import by.afinny.apigateway.dto.userservice.constant.AuthenticationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PUBLIC)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for authentication")
public class LoginDto {

    @Schema(description = "login")
    private String login;
    @Schema(description = "password")
    private String password;
    @Schema(description = "authentication type")
    private AuthenticationType type;
}
