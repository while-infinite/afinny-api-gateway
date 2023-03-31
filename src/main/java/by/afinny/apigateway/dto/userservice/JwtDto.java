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
@Schema(description = "DTO with access and refresh tokens")
public class JwtDto {

    @Schema(description = "token for application access")
    private String accessToken;
    @Schema(description = "token for refreshing")
    private String refreshToken;
}
