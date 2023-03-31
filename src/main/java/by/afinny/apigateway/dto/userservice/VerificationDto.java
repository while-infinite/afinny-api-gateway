package by.afinny.apigateway.dto.userservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@Schema(description = "DTO for receiver and verification code")
public class VerificationDto {

    @Schema(description = "email or phone number")
    private String mobilePhone;
    @Schema(description = "verification code")
    private String verificationCode;
}
