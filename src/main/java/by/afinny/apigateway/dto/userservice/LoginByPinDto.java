package by.afinny.apigateway.dto.userservice;

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
@Schema(description = "DTO for authentication by pin")
public class LoginByPinDto {

    @Schema(description = "token")
    private String token;
    @Schema(description = "fingerprint")
    private String fingerprint;
}
