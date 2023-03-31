package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.AutoInsuranceDocumentDto;
import by.afinny.apigateway.openfeign.userservice.InsuranceDocumentClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsuranceDocumentController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class InsuranceDocumentControllerTest {

    @MockBean
    private InsuranceDocumentClient insuranceDocumentClient;
    @Autowired
    private MockMvc mockMvc;

    private final UUID documentId = UUID.randomUUID();
    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private MockMultipartFile file;
    private AutoInsuranceDocumentDto autoInsuranceDocumentDto;

    @BeforeAll
    void setUp() {
        file = new MockMultipartFile("passportOwnerPage",
                "passport_owner_page.jpg",
                "image/jpeg",
                "Hello, World!".getBytes());
        autoInsuranceDocumentDto = AutoInsuranceDocumentDto.builder()
                .documentName("passportOwnerPage")
                .file(file)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser
    @DisplayName("If request for deleting auto insurance document proceed then status should be redirected")
    void deleteAutoInsuranceDocument_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(insuranceDocumentClient.deleteDocument(documentId.toString()))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete(InsuranceDocumentController.INSURANCE_DOCUMENTS_URL + "/" + documentId));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for uploading auto insurance documents proceed then status must be redirected")
    void uploadingAutoInsuranceDocuments_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(insuranceDocumentClient.uploadingDocument(any(UUID.class), any(AutoInsuranceDocumentDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post(InsuranceDocumentController.INSURANCE_DOCUMENTS_URL + "/new")
                        .param("clientId", TEST_CLIENT_ID)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .sessionAttr("file", autoInsuranceDocumentDto))
                .andExpect(status().is(httpStatus));
    }
}
