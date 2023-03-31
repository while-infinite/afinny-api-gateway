package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.ChangingEmailDto;
import by.afinny.apigateway.dto.userservice.MobilePhoneDto;
import by.afinny.apigateway.dto.userservice.NotificationChangerDto;
import by.afinny.apigateway.dto.userservice.NotificationDto;
import by.afinny.apigateway.dto.userservice.PasswordDto;
import by.afinny.apigateway.dto.userservice.SecurityDto;
import by.afinny.apigateway.openfeign.userservice.UserClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/user/settings")
@RequiredArgsConstructor
@Tag(
        name = "User Controller", description = "Allow to change settings at application's user profile"
)
public class UserController {

    private final UserClient userClient;

    @Operation(summary = "Changes password", description = "Compare incoming and stored old passwords and update entry in DB")
    @ApiResponse(responseCode = "200", description = "Password has been updated",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = PasswordDto.class))})
    @PatchMapping("password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Parameter(description = "old and new passwords") PasswordDto passwordDto,
            Authentication authentication) {
        return userClient.changePassword(passwordDto, getClientId(authentication));
    }

    @Operation(summary = "Changes security data", description = "Update security question and answer in DB")
    @ApiResponse(responseCode = "200", description = "Security data has been updated",
                 content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SecurityDto.class))})
    @PatchMapping("controls")
    public ResponseEntity<Void> changeSecurityData(
            @RequestBody @Parameter(description = "security question and answer") SecurityDto securityDto,
            Authentication authentication) {
        return userClient.changeSecurityData(securityDto, getClientId(authentication));
    }

    @Operation(summary = "Process Client data", description = "Get client notification settings")
    @ApiResponse(responseCode = "200", description = "Notifications has been sent",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = NotificationDto.class))})
    @GetMapping("notifications/all")
    public ResponseEntity<NotificationDto> getNotificationSettings(Authentication authentication) {
        return userClient.getNotifications(getClientId(authentication));
    }

    @Operation(summary = "Process Client data (sms)", description = "Change sms notification settings")
    @ApiResponse(responseCode = "200", description = "Sms notification has been successfully changed",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = NotificationChangerDto.class))})
    @PatchMapping("notifications/sms")
    public ResponseEntity<Void> changeSmsNotificationSettings(
            @RequestBody @Parameter(description = "notification status") NotificationChangerDto notificationChangerDto,
            Authentication authentication) {
        return userClient.changeSmsNotification(notificationChangerDto, getClientId(authentication));
    }

    @Operation(summary = "Process Client data (push)", description = "Change push notification settings")
    @ApiResponse(responseCode = "200", description = "Push notification has been successfully changed",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = NotificationChangerDto.class))})
    @PatchMapping("notifications/push")
    public ResponseEntity<Void> changePushNotificationSettings(
            @RequestBody @Parameter(description = "notification status") NotificationChangerDto notificationChangerDto,
            Authentication authentication) {
        return userClient.changePushNotification(notificationChangerDto, getClientId(authentication));
    }

    @Operation(summary = "Update email", description = "Changing an existing email or setting if it has not been set")
    @ApiResponse(responseCode = "200", description = "Email has been successfully changed",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ChangingEmailDto.class))})
    @PatchMapping("email")
    public ResponseEntity<Void> changeEmail(
            @RequestBody @Parameter(description = "newEmail") ChangingEmailDto changingEmailDto,
            Authentication authentication) {
        return userClient.changeEmail(changingEmailDto, getClientId(authentication));
    }

    @Operation(summary = "Set email notifications", description = "Enable or disable email notification for specified client")
    @ApiResponse(responseCode = "200", description = "Email notification has been successfully changed",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = NotificationChangerDto.class))})
    @PatchMapping("notifications/email")
    public ResponseEntity<Void> setEmailNotification(
            @RequestBody @Parameter(description = "notification status") NotificationChangerDto notificationChangerDto,
            Authentication authentication) {
        return userClient.setEmailSubscription(notificationChangerDto, getClientId(authentication));
    }
    @Operation(summary = "Update mobile phone", description = "Changing an existing phone or setting if it has not been set")
    @ApiResponse(responseCode = "200", description = "Mobile phone has been successfully changed")
    @PatchMapping("phone")
    public ResponseEntity<Void> changeMobilePhone(
            @RequestBody @Parameter(description = "mobilePhone") MobilePhoneDto mobilePhoneDto,
            Authentication authentication) {
        return userClient.changeMobilePhone(mobilePhoneDto, getClientId(authentication));
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
