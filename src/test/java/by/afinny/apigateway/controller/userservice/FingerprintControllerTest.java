package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.FingerprintDto;
import by.afinny.apigateway.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FingerprintController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class FingerprintControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";

    private FingerprintDto fingerprintDto;

    @BeforeAll
    void createDto() {
        fingerprintDto = FingerprintDto.builder()
                .fingerprint("fingerprint")
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 409, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if create new fingerprint then status should be redirected")
    void createFingerprint_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(authenticationService.createFingerprint(any(FingerprintDto.class), any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post(FingerprintController.URL_FINGERPRINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(fingerprintDto)))
                .andExpect(status().is(httpStatus));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }
}