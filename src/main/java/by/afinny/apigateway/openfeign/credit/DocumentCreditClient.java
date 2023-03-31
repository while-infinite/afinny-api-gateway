package by.afinny.apigateway.openfeign.credit;

import by.afinny.apigateway.config.openfeign.FeignConfig;
import by.afinny.apigateway.dto.credit.RequestCreditOrderDocumentDto;
import by.afinny.apigateway.dto.credit.ResponseCreditOrderDocument;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "CREDIT/auth/credit-order-documents", configuration = FeignConfig.class)
public interface DocumentCreditClient {

    @PostMapping(value = "new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> uploadingDocuments(@RequestParam UUID clientId, @RequestParam UUID creditOrderId,
                                            @RequestBody RequestCreditOrderDocumentDto
                                                    creditOrderDocument);

    @DeleteMapping("/{documentId}")
    ResponseEntity<Void> deleteDocument(@RequestParam UUID clientId, @PathVariable UUID documentId);

    @GetMapping("/{clientId}")
    ResponseEntity<List<ResponseCreditOrderDocument>> getClientDocument(@PathVariable UUID clientId);

}
