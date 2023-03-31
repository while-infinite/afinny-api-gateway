package by.afinny.apigateway.openfeign.userservice;

import by.afinny.apigateway.config.openfeign.FeignConfig;
import by.afinny.apigateway.dto.userservice.ResponseVerificationDocumentsDto;
import by.afinny.apigateway.dto.userservice.VerificationDocumentsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(value = "USER-SERVICE/auth/verification-documents", configuration = FeignConfig.class)
public interface DocumentClient {

    @PostMapping(value = "new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> uploadingDocuments(@RequestParam UUID clientId,
                                            @RequestBody VerificationDocumentsDto verificationDocumentsDto);

    @DeleteMapping("{documentId}")
    ResponseEntity<Void> deleteDocument(@PathVariable String documentId);

    @GetMapping("{clientId}")
    ResponseEntity<ResponseVerificationDocumentsDto> getDocuments(@PathVariable UUID clientId);
}
