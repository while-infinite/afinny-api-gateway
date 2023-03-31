package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.MobilePhoneDto;
import by.afinny.apigateway.dto.userservice.PassportDto;
import by.afinny.apigateway.dto.userservice.SmsBlockExpirationDto;
import by.afinny.apigateway.dto.userservice.VerificationDto;
import by.afinny.apigateway.openfeign.userservice.VerificationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/security/session")
@Tag(
        name = "Verification Controller", description = "Send and verify code"
)
public class VerificationController {

    private final VerificationClient verificationClient;

    @Operation(summary = "Send Verification Code", description = "Generate verification code and send it to receiver")
    @ApiResponse(responseCode = "200", description = "Verification code has been successfully sent")
    @PatchMapping
    public ResponseEntity<SmsBlockExpirationDto> sendVerificationCode(
            @RequestParam @Parameter(description = "Phone number") String receiver) {

        return verificationClient.sendVerificationCode(receiver);
    }

    @Operation(summary = "Verify entered Code", description = "Compare entered and sent codes")
    @ApiResponse(responseCode = "200", description = "Verification code has been successfully sent")
    @ApiResponse(responseCode = "406", description = "User in block")
    @PostMapping("verification")
    public ResponseEntity<Void> checkVerificationCode(
            @RequestBody @Parameter(description = "Receiver and verification code") VerificationDto verificationDto) {

        return verificationClient.checkVerificationCode(verificationDto);
    }

    @Operation(summary = "Send phone number to block", description = "Send mobile phone number for temporary blocking")
    @ApiResponse(responseCode = "200", description = "Mobile phone number has been successfully sent")
    @ApiResponse(responseCode = "400", description = "There is no code sent to receiver")
    @PatchMapping("verification")
    public ResponseEntity<Void> setUserBlockTimestamp(
            @RequestBody @Parameter(description = "Mobile phone number") MobilePhoneDto mobilePhone) {

        return verificationClient.setUserBlockTimestamp(mobilePhone);
    }

    @Operation(summary = "Get mobile phone from passport number", description = "Get client's mobile phone")
    @ApiResponse(responseCode = "200", description = "Mobile phone has been successfully sent")
    @ApiResponse(responseCode = "400", description = "Invalid passport number")
    @PostMapping
    public ResponseEntity<MobilePhoneDto> getMobilePhoneFromPassport(
            @RequestBody @Parameter(description = "Passport number") PassportDto passportNumber) {

        return verificationClient.getMobilePhoneFromPassport(passportNumber);
    }
}
