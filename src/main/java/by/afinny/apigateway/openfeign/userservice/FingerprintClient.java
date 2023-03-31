package by.afinny.apigateway.openfeign.userservice;

import by.afinny.apigateway.dto.userservice.RequestFingerprintDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("USER-SERVICE/fingerprint")
public interface FingerprintClient {

    @PostMapping()
    ResponseEntity<Void> createFingerprint(@RequestBody RequestFingerprintDto fingerprintDto);
}
