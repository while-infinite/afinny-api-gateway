package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.RequestCreditOrderDocumentDto;
import by.afinny.apigateway.dto.credit.ResponseCreditOrderDocument;
import by.afinny.apigateway.openfeign.credit.DocumentCreditClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentCreditController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class DocumentCreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentCreditClient documentCreditClient;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";

    private final String TEST_CREDIT_ORDER_ID = "00000000-0000-0000-0000-000000000008";
    private final UUID DOCUMENT_ID = UUID.randomUUID();
    private final UUID CLIENT_ID = UUID.randomUUID();

    private MockMultipartFile file;

    private RequestCreditOrderDocumentDto creditOrderDocumentDto;
    private ResponseCreditOrderDocument responseCreditOrderDocument;

    @BeforeAll
    void setUp() {
        file = new MockMultipartFile("file",
                "document.jpg",
                "image/jpeg",
                "Hello, World!".getBytes());

        creditOrderDocumentDto = RequestCreditOrderDocumentDto.builder()
                .file(file)
                .build();

        responseCreditOrderDocument = ResponseCreditOrderDocument.builder()
                .id(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .documentName("name")
                .creditOrderId(UUID.randomUUID())
                .file(new byte[]{1, 2, 3})
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for uploading documents proceed then status must be redirected")
    void uploadingDocuments_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(documentCreditClient.uploadingDocuments(any(UUID.class), any(UUID.class), any(RequestCreditOrderDocumentDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/credit-order-documents/new")
                        .param("clientId", TEST_CLIENT_ID)
                        .param("creditOrderId", TEST_CREDIT_ORDER_ID)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .sessionAttr("files", creditOrderDocumentDto))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {204, 401, 400, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for delete credit order proceed then status must be redirected")
    void deleteDocument_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(documentCreditClient.deleteDocument(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity
                .status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete(DocumentCreditController.URL_CREDIT_DOCUMENT
                        + DocumentCreditController.URL_DOCUMENT_ID, DOCUMENT_ID)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting  documents proceed then body should be returned with docs")
    void findDocument_thenReturnResponseClientDocument() throws Exception {
        //ARRANGE
        when(documentCreditClient.getClientDocument(CLIENT_ID)).thenReturn(ResponseEntity.ok(List.of(responseCreditOrderDocument)));

        //ACT
        MvcResult result = mockMvc.perform(get("/api/v1/credit-order-documents/" + CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(List.of(responseCreditOrderDocument)), result.getResponse().getContentAsString());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {

        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {

        AssertionsForClassTypes.assertThat(actualBody).isEqualTo(expectedBody);
    }

}
