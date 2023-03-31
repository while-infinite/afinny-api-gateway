package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.VerificationDocumentsDto;
import by.afinny.apigateway.openfeign.userservice.DocumentClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import by.afinny.apigateway.dto.userservice.ResponseVerificationDocumentsDto;
import by.afinny.apigateway.dto.userservice.VerificationDocumentDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DocumentControllerTest {

    @MockBean
    private DocumentClient documentClient;
    @Autowired
    private MockMvc mockMvc;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";

    private MockMultipartFile page3;
    private MockMultipartFile registrationPage;
    private VerificationDocumentsDto verificationDocumentsDto;
    private ResponseVerificationDocumentsDto responseDocumentsDto;
    private final UUID documentId = UUID.randomUUID();
    private final UUID clientId = UUID.randomUUID();

    @BeforeAll
    void setUp() {
        page3 = new MockMultipartFile("page3",
                "passport_page_3.jpg",
                "image/jpeg",
                "Hello, World!".getBytes());
        registrationPage = new MockMultipartFile("registrationPage ",
                "passport_registration_page.jpg",
                "image/jpeg",
                "Hello, World!".getBytes());
        verificationDocumentsDto = VerificationDocumentsDto.builder()
                .page3(page3)
                .registrationPage(registrationPage)
                .build();
        VerificationDocumentDto document = VerificationDocumentDto.builder()
                .clientId(clientId)
                .id(documentId)
                .build();
        responseDocumentsDto = ResponseVerificationDocumentsDto.builder()
                .page3(document)
                .registrationPage(document)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for uploading documents proceed then status must be redirected")
    void uploadingDocuments_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(documentClient.uploadingDocuments(any(UUID.class), any(VerificationDocumentsDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/verification-documents/new")
                        .param("clientId", TEST_CLIENT_ID)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .sessionAttr("files", verificationDocumentsDto))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser
    @DisplayName("If request for deleting verification document proceed then status should be redirected")
    void deleteDocument_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(documentClient.deleteDocument(documentId.toString()))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete(DocumentController.VERIFICATION_DOCUMENTS_URL + "/" + documentId))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting verification documents proceed then body should be returned with docs")
    void getDocuments_shouldReturnBodyWithDocuments () throws Exception {
        //ARRANGE
        when(documentClient.getDocuments(any(UUID.class))).thenReturn(ResponseEntity.ok(responseDocumentsDto));

        //ACT
        MvcResult result = mockMvc.perform(
                get(DocumentController.VERIFICATION_DOCUMENTS_URL))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseDocumentsDto), result.getResponse().getContentAsString());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}
