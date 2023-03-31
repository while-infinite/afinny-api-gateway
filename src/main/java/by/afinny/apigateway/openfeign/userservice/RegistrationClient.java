package by.afinny.apigateway.openfeign.userservice;

import by.afinny.apigateway.dto.userservice.ClientDto;
import by.afinny.apigateway.dto.userservice.PassportDto;
import by.afinny.apigateway.dto.userservice.RegisteringUserDto;
import by.afinny.apigateway.dto.userservice.RequestClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("USER-SERVICE/registration")
public interface RegistrationClient {

    @GetMapping
    ResponseEntity<ClientDto> verifyMobilePhone(@RequestParam(name = "mobilePhone") String mobilePhone);

    @PatchMapping("user-profile")
    ResponseEntity<Void> registerExistingClient(RegisteringUserDto registeringUser);

    @PostMapping("user-profile/new")
    ResponseEntity<Void> registerNonClient(RequestClientDto requestClientDto);

    @PostMapping("user-profile/verification")
    ResponseEntity<Void> verifyPassportNumber(PassportDto passportNumber);
}
