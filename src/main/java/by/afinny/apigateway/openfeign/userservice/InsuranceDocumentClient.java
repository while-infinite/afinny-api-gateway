package by.afinny.apigateway.openfeign.userservice;

import by.afinny.apigateway.config.openfeign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import by.afinny.apigateway.dto.userservice.AutoInsuranceDocumentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(value = "USER-SERVICE/auth/insurance-documents", configuration = FeignConfig.class)
public interface InsuranceDocumentClient {

    @DeleteMapping("{documentId}")
    ResponseEntity<Void> deleteDocument(@PathVariable String documentId);

    @PostMapping(value = "new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> uploadingDocument(@RequestParam UUID clientId,
                                            @RequestBody AutoInsuranceDocumentDto autoInsuranceDocumentDto);

}
