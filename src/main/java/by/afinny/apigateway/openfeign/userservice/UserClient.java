package by.afinny.apigateway.openfeign.userservice;

import by.afinny.apigateway.dto.userservice.ChangingEmailDto;
import by.afinny.apigateway.dto.userservice.MobilePhoneDto;
import by.afinny.apigateway.dto.userservice.NotificationChangerDto;
import by.afinny.apigateway.dto.userservice.NotificationDto;
import by.afinny.apigateway.dto.userservice.PasswordDto;
import by.afinny.apigateway.dto.userservice.SecurityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("USER-SERVICE/auth/user/settings")
public interface UserClient {

    @PatchMapping("password")
    ResponseEntity<Void> changePassword(PasswordDto passwordDto, @RequestParam UUID clientId);

    @PatchMapping("controls")
    ResponseEntity<Void> changeSecurityData(SecurityDto securityDto, @RequestParam UUID clientId);

    @GetMapping("notifications/all")
    ResponseEntity<NotificationDto> getNotifications(@RequestParam UUID clientId);

    @PatchMapping("notifications/sms")
    ResponseEntity<Void> changeSmsNotification(NotificationChangerDto notificationChangerDto, @RequestParam UUID clientId);

    @PatchMapping("notifications/push")
    ResponseEntity<Void> changePushNotification(NotificationChangerDto notificationChangerDto, @RequestParam UUID clientId);

    @PatchMapping("email")
    ResponseEntity<Void> changeEmail(ChangingEmailDto changingEmailDto, @RequestParam UUID clientId);

    @PatchMapping("notifications/email")
    ResponseEntity<Void> setEmailSubscription(NotificationChangerDto notificationChangerDto, @RequestParam UUID clientId);

    @PatchMapping("phone")
    ResponseEntity<Void> changeMobilePhone(@RequestBody MobilePhoneDto mobilePhoneDto, @RequestParam UUID clientId);
}
