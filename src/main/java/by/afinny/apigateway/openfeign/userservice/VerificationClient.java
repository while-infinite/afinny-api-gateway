package by.afinny.apigateway.openfeign.userservice;

import by.afinny.apigateway.dto.userservice.MobilePhoneDto;
import by.afinny.apigateway.dto.userservice.PassportDto;
import by.afinny.apigateway.dto.userservice.SmsBlockExpirationDto;
import by.afinny.apigateway.dto.userservice.VerificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("USER-SERVICE/security/session")
public interface VerificationClient {

    @PatchMapping
    ResponseEntity<SmsBlockExpirationDto> sendVerificationCode(@RequestParam String receiver);

    @PostMapping("verification")
    ResponseEntity<Void> checkVerificationCode(VerificationDto verificationDto);

    @PatchMapping("verification")
    ResponseEntity<Void> setUserBlockTimestamp(MobilePhoneDto mobilePhone);

    @PostMapping
    ResponseEntity<MobilePhoneDto> getMobilePhoneFromPassport(PassportDto passportNumber);
}
