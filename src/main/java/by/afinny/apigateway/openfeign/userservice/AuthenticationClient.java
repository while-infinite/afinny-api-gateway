package by.afinny.apigateway.openfeign.userservice;

import by.afinny.apigateway.dto.userservice.LoginDto;
import by.afinny.apigateway.dto.userservice.RequestLoginByPinDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@FeignClient("USER-SERVICE/login")
public interface AuthenticationClient {

    @PostMapping()
    ResponseEntity<UUID> authenticateUser(LoginDto loginDto);

    @PostMapping("pin")
    ResponseEntity<UUID> authenticateUserByPin(RequestLoginByPinDto loginByPinDto);

    @PatchMapping("password")
    ResponseEntity<Void> setNewPassword(@RequestParam(name = "mobilePhone") String mobilePhone, String newPassword);
}
