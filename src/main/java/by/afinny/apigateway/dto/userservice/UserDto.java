package by.afinny.apigateway.dto.userservice;

import by.afinny.apigateway.dto.userservice.constant.ClientStatus;
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
@Schema(description = "DTO for response user")
public class UserDto {

    @Schema(description = "mobile phone number")
    protected String mobilePhone;
    @Schema(description = "client status")
    protected ClientStatus clientStatus;
}
