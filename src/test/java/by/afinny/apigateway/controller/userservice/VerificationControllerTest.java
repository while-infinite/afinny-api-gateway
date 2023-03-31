package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.MobilePhoneDto;
import by.afinny.apigateway.dto.userservice.PassportDto;
import by.afinny.apigateway.dto.userservice.VerificationDto;
import by.afinny.apigateway.openfeign.userservice.VerificationClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerificationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VerificationClient verificationClient;

    private final String MOBILE_PHONE = "79023451191";
    private final String VERIFICATION_CODE = "287109";
    private final String PASSPORT_NUMBER = "1234567891";

    @Captor
    private ArgumentCaptor<String> receiverCaptor;
    @Captor
    private ArgumentCaptor<MobilePhoneDto> mobilePhoneDtoCaptor;
    @Captor
    private ArgumentCaptor<PassportDto> passportDtoCaptor;

    private MobilePhoneDto mobilePhoneDto;
    private VerificationDto verificationDto;
    private PassportDto passportDto;

    @BeforeEach
    void setUp() {
        mobilePhoneDto = MobilePhoneDto.builder()
                .mobilePhone(MOBILE_PHONE)
                .build();
        verificationDto = VerificationDto.builder()
                .mobilePhone(MOBILE_PHONE)
                .verificationCode(VERIFICATION_CODE).build();
        passportDto = PassportDto.builder()
                .passportNumber(PASSPORT_NUMBER).build();
    }

    @Test
    @DisplayName("If request for sending code proceed then status should be be OK")
    void sendVerificationCode_shouldReturnStatusOK() throws Exception {
        //ARRANGE
        when(verificationClient.sendVerificationCode(receiverCaptor.capture()))
                .thenReturn(ResponseEntity.ok().build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/security/session")
                        .param("receiver", MOBILE_PHONE))
                .andExpect(status().isOk())
                .andReturn();
        verifyReceiverParameter(receiverCaptor.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @DisplayName("If request for verification check proceed then status should be redirected")
    void checkVerificationCode_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(verificationClient.checkVerificationCode(any(VerificationDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/security/session/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(verificationDto)))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser
    @DisplayName("If setting user temporary block request proceed then status should be redirected")
    void setUserBlockTimestamp_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(verificationClient.setUserBlockTimestamp(mobilePhoneDtoCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/security/session/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mobilePhoneDto)))
                .andExpect(status().is(httpStatus))
                .andReturn();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 500})
    @DisplayName("If request for getting mobile phone proceed then status must be redirected")
    void getMobilePhoneFromPassport_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(verificationClient.getMobilePhoneFromPassport(passportDtoCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/security/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passportDto)))
                .andExpect(status().is(httpStatus))
                .andReturn();
    }

    @Test
    @DisplayName("If request for getting mobile phone proceed then response body must be redirected")
    void getMobilePhoneFromPassport_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(verificationClient.getMobilePhoneFromPassport(passportDtoCaptor.capture()))
                .thenReturn(ResponseEntity.ok(mobilePhoneDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(post("/api/v1/security/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passportDto)))
                .andExpect(status().isOk())
                .andReturn();

        verifyPassportDtoParameter(passportDtoCaptor.getValue());
        verifyBody(asJsonString(mobilePhoneDto), result.getResponse().getContentAsString());
    }

    private void verifyPassportDtoParameter(PassportDto passportDtoParam) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(passportDtoParam)
                    .withFailMessage("Passport dto parameter should not be null")
                    .isNotNull();
            softAssertions.assertThat(passportDtoParam.toString())
                    .withFailMessage("Passport dto should be " + passportDto.toString()
                            + " instead of " + passportDtoParam.toString())
                    .isEqualTo(passportDto.toString());
            softAssertions.assertAll();
        });
    }

    private void verifyReceiverParameter(String receiverParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(receiverParameter)
                .withFailMessage("Receiver parameter should not be null")
                .isNotNull();
        softAssertions.assertThat(receiverParameter)
                .withFailMessage("Receiver should be " + MOBILE_PHONE + " instead of " + receiverParameter)
                .isEqualTo(MOBILE_PHONE);
        softAssertions.assertAll();
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}
