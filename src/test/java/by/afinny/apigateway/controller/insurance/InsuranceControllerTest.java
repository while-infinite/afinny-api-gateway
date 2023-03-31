package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.PolicyInfoDto;
import by.afinny.apigateway.dto.insurance.ResponseApplicationInsuranceTypeDto;
import by.afinny.apigateway.dto.insurance.ResponsePaymentDetailsDto;
import by.afinny.apigateway.dto.insurance.ResponseRejectionLetterDto;
import by.afinny.apigateway.dto.insurance.ResponseUserPolicyDto;
import by.afinny.apigateway.dto.insurance.constant.InsuranceStatus;
import by.afinny.apigateway.dto.insurance.constant.InsuranceType;
import by.afinny.apigateway.openfeign.insurance.InsuranceClient;
import by.afinny.apigateway.service.insurance.InsuranceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsuranceController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceClient insuranceClient;

    @MockBean
    private InsuranceService insuranceService;

    private List<ResponseUserPolicyDto> userPolicyDtoList;

    private List<ResponseApplicationInsuranceTypeDto> insuranceTypeDtoList;

    private ResponseRejectionLetterDto responseRejectionLetterDto;

    private ResponsePaymentDetailsDto paymentDetailsDto;

    private PolicyInfoDto policyInfoDto;

    private final UUID APPLICATION_ID = UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb");

    private final String TEST_CLIENT_ID = "cc6588da-ffaf-4c00-a3bd-2e0c6d83655d";

    @BeforeEach
    void setUp() {
        userPolicyDtoList = List.of(ResponseUserPolicyDto.builder()
                        .insuranceType(InsuranceType.MEDICAL_INSURANCE)
                        .registrationDate(LocalDate.of(2022, 10, 15))
                        .insuranceStatus(InsuranceStatus.PENDING)
                        .number("12345")
                        .agreementDate(LocalDate.of(2022, 3, 23))
                        .build(),
                ResponseUserPolicyDto.builder()
                        .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                        .registrationDate(LocalDate.of(2022, 5, 10))
                        .insuranceStatus(InsuranceStatus.APPROVED)
                        .number("67890")
                        .agreementDate(LocalDate.of(2022, 5, 5))
                        .build());

        ResponseApplicationInsuranceTypeDto insuranceTypeDto = ResponseApplicationInsuranceTypeDto.builder()
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .build();

        responseRejectionLetterDto = ResponseRejectionLetterDto.builder().id(APPLICATION_ID)
                .failureDiagnosisReport("Hi was bad")
                .build();

        insuranceTypeDtoList = List.of(insuranceTypeDto);

        paymentDetailsDto = ResponsePaymentDetailsDto.builder()
                .policySum(new BigDecimal("2.00"))
                .insuranceType(InsuranceType.MEDICAL_INSURANCE)
                .build();

        policyInfoDto = PolicyInfoDto.builder()
                .insuranceStatus("APPROVED")
                .number("1")
                .agreementDate(LocalDate.of(2022, 11, 12))
                .startDate(LocalDate.of(2023, 10, 11))
                .periodOfInsurance("ONE_MONTH")
                .paymentCycle("THREE_MONTHS")
                .insuranceSum(BigDecimal.valueOf(20))
                .policySum(BigDecimal.valueOf(2))
                .region("1")
                .district("1")
                .city("1")
                .street("1")
                .houseNumber("1")
                .flatNumber("1")
                .model("model")
                .carNumber("2222")
                .clientId("222222")
                .isFlat(true)
                .firstName("1")
                .middleName("1")
                .lastName("1")
                .build();
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting policy information proceed then response body must be redirected")
    void getCardInformation_shouldRedirectResponseBody() throws Exception {

        //ARRANGE
        when(insuranceClient.getPolicyInformation(UUID.fromString(TEST_CLIENT_ID), APPLICATION_ID)).thenReturn(ResponseEntity.ok(policyInfoDto));

        //ACT
        MvcResult result = mockMvc.perform(
                        get("/api/v1/insurance/{applicationId}", APPLICATION_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(policyInfoDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting policy information proceed then status should be redirected")
    void getPolicyInformation_shouldRedirectStatus(int status) throws Exception {
        //ARRANGE
        when(insuranceClient.getPolicyInformation(UUID.fromString(TEST_CLIENT_ID), APPLICATION_ID))
                .thenReturn(ResponseEntity.status(status).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/insurance/{applicationId}", APPLICATION_ID))
                .andExpect(status().is(status));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser
    @DisplayName("If request for getting insurance types proceed then status should be redirected")
    void getInsuranceTypes_shouldRedirectStatus(int status) throws Exception {
        //ARRANGE
        when(insuranceClient.getInsuranceTypes())
                .thenReturn(ResponseEntity.status(status).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/insurance/types"))
                .andExpect(status().is(status));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting insurance user policies proceed then status should be redirected")
    void getUserPolicies_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(insuranceClient.getUserPolicies(any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/insurance")
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));

    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting insurance user policies proceed then response body should be redirected")
    void getUserPolicies_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(insuranceClient.getUserPolicies(UUID.fromString(TEST_CLIENT_ID)))
                .thenReturn(ResponseEntity.ok(userPolicyDtoList));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/insurance/")
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(userPolicyDtoList), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting insurance types proceed then response body should be redirected")
    void getInsuranceTypes_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(insuranceClient.getInsuranceTypes())
                .thenReturn(ResponseEntity.ok(insuranceTypeDtoList));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/insurance/types"))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(insuranceTypeDtoList), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting rejection letter proceed then status should be redirected")
    void getRejectionLetter_shouldRedirectStatus(int status) throws Exception {
        //ARRANGE
        when(insuranceClient.getRejectionLetter(UUID.fromString(TEST_CLIENT_ID), APPLICATION_ID))
                .thenReturn(ResponseEntity.status(status).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/insurance/{application-id}/report", APPLICATION_ID))
                .andExpect(status().is(status));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting rejection letter proceed then response body should be redirected")
    void getRejectionLetter_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(insuranceClient.getRejectionLetter(UUID.fromString(TEST_CLIENT_ID), APPLICATION_ID))
                .thenReturn(ResponseEntity.ok(responseRejectionLetterDto));
        //ACT & VERIFY
        MvcResult mvcResult =
                mockMvc.perform(get("/api/v1/insurance/{application-id}/report", APPLICATION_ID))
                        .andExpect(status().isOk())
                        .andReturn();
        verifyBody(asJsonString(responseRejectionLetterDto), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting payment details proceed then status should be redirected")
    void getPaymentDetails_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(insuranceClient.getPaymentDetails(any(UUID.class), any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/insurance/{application-id}/payment-details", APPLICATION_ID))
                .andExpect(status().is(httpStatus));

    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting payment details proceed then response body should be redirected")
    void getPaymentDetails_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(insuranceClient.getPaymentDetails(UUID.fromString(TEST_CLIENT_ID), APPLICATION_ID))
                .thenReturn(ResponseEntity.ok(paymentDetailsDto));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/insurance/{application-id}/payment-details", APPLICATION_ID))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(paymentDetailsDto), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for cancelling application proceed then status should be redirected")
    void cancelPolicyApplication_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(insuranceClient.cancelPolicyApplication(any(UUID.class), any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete("/api/v1/insurance/{application-id}/revocation", APPLICATION_ID))
                .andExpect(status().is(httpStatus));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}