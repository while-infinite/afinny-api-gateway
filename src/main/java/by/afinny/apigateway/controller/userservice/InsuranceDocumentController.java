package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.AutoInsuranceDocumentDto;
import by.afinny.apigateway.openfeign.userservice.InsuranceDocumentClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/insurance-documents")
@Tag(name = "Insurance Document Controller", description = "Manage actions with auto insurance document")
public class InsuranceDocumentController {

    private final InsuranceDocumentClient insuranceDocumentClient;

    public final static String INSURANCE_DOCUMENTS_URL = "/api/v1/insurance-documents";

    @Operation(summary = "Deleting auto insurance document",
               description = "Deleting uploaded auto insurance document")
    @ApiResponse(responseCode = "204", description = "Uploaded auto insurance document has been deleted")
    @DeleteMapping("{documentId}")
    public ResponseEntity<Void> deleteAutoInsuranceDocument(@PathVariable String documentId) {
        return insuranceDocumentClient.deleteDocument(documentId);
    }

    @Operation(summary = "Uploading auto insurance document", description = "Uploading auto insurance document")
    @ApiResponse(responseCode = "200", description = "Document has been successfully uploaded")
    @PostMapping("new")
    public ResponseEntity<Void> uploadingDocuments(Authentication authentication,
                                                   @ModelAttribute AutoInsuranceDocumentDto autoInsuranceDocumentDto) {
        return insuranceDocumentClient.uploadingDocument(getClientId(authentication), autoInsuranceDocumentDto);
    }

    private UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
