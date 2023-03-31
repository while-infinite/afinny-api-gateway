package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.RequestCreditOrderDocumentDto;
import by.afinny.apigateway.dto.credit.ResponseCreditOrderDocument;
import by.afinny.apigateway.openfeign.credit.DocumentCreditClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/credit-order-documents")
@Tag(name = "Documents Controller", description = "Manage work with credit documents")
public class DocumentCreditController {

    private final DocumentCreditClient documentCreditClient;

    public static final String URL_CREDIT_DOCUMENT = "/api/v1/credit-order-documents";
    public static final String URL_DOCUMENT_ID = "/{documentId}";

    @Operation(summary = "Upload document", description = "Uploading document")
    @ApiResponse(responseCode = "204", description = "Credit document has been uploaded")
    @PostMapping("new")
    public ResponseEntity<Void> uploadingDocuments(Authentication authentication, @RequestParam UUID creditOrderId,
                                                   @ModelAttribute RequestCreditOrderDocumentDto creditOrderDocumentDto) {
        return documentCreditClient.uploadingDocuments(getClientId(authentication), creditOrderId, creditOrderDocumentDto);
    }

    @Operation(summary = "Deleting document", description = "Deleting uploaded  document")
    @ApiResponse(responseCode = "204", description = "Uploaded credit document has been deleted")
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteCreditDocument(Authentication authentication, @PathVariable UUID documentId) {
        return documentCreditClient.deleteDocument(getClientId(authentication), documentId);
    }

    @Operation(summary = "Getting document", description = "Getting client document")
    @ApiResponse(responseCode = "204", description = "Document has been found")
    @GetMapping("/{clientId}")
    public ResponseEntity<List<ResponseCreditOrderDocument>> getClientDocument(@PathVariable UUID clientId) {
        return documentCreditClient.getClientDocument(clientId);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
