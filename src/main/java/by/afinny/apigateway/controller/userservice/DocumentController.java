package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.ResponseVerificationDocumentsDto;
import by.afinny.apigateway.dto.userservice.VerificationDocumentsDto;
import by.afinny.apigateway.openfeign.userservice.DocumentClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/verification-documents")
@Tag(name = "Document Controller", description = "Manage actions with documents sent for verification")
public class DocumentController {

    private final DocumentClient documentClient;

    public final static String VERIFICATION_DOCUMENTS_URL = "/api/v1/verification-documents";

    @Operation(summary = "Deleting document", description = "Deleting uploaded verification document")
    @ApiResponse(responseCode = "204", description = "Uploaded verification document has been deleted")
    @DeleteMapping("{documentId}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable String documentId) {
        return documentClient.deleteDocument(documentId);
    }
    @Operation(summary = "Getting documents", description = "Getting verification documents by client id")
    @ApiResponse(responseCode = "200", description = "Ok")
    @GetMapping()
    public ResponseEntity<ResponseVerificationDocumentsDto> getDocuments(Authentication authentication) {
        return documentClient.getDocuments(getClientId(authentication));
    }

    @Operation(summary = "Uploading documents", description = "Uploading documents for verification")
    @ApiResponse(responseCode = "200", description = "Documents has been successfully uploaded")
    @PostMapping("new")
    public ResponseEntity<Void> uploadingDocuments(Authentication authentication,
                                                   @ModelAttribute VerificationDocumentsDto verificationDocumentsDto) {
        return documentClient.uploadingDocuments(getClientId(authentication), verificationDocumentsDto);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
