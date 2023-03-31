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
@Schema(description = "DTO outgoing data of client's notification")
public class NotificationDto {

    @Schema(description = "email")
    private String email;
    @Schema(description = "sms notification")
    private boolean smsNotification;
    @Schema(description = "push notification")
    private boolean pushNotification;
    @Schema(description = "email subscription")
    private boolean emailSubscription;
}
