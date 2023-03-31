package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.CreditBalanceDto;
import by.afinny.apigateway.dto.credit.CreditCardBalanceDto;
import by.afinny.apigateway.dto.credit.CreditDto;
import by.afinny.apigateway.dto.credit.CreditScheduleDto;
import by.afinny.apigateway.dto.credit.DetailsDto;
import by.afinny.apigateway.dto.credit.PaymentScheduleDto;
import by.afinny.apigateway.dto.credit.ResponseOperationDto;
import by.afinny.apigateway.openfeign.credit.CreditClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CreditControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreditClient creditClient;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final UUID TEST_CREDIT_ID = UUID.randomUUID();
    private final UUID CLIENT_ID = UUID.randomUUID();
    private final UUID TEST_AGREEMENT_ID = UUID.randomUUID();

    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;
    @Captor
    private ArgumentCaptor<UUID> creditIdCaptor;
    @Captor
    private ArgumentCaptor<UUID> agreementIdCaptor;
    private List<CreditDto> responseCredits;
    private CreditBalanceDto creditBalanceDto;
    private CreditScheduleDto creditScheduleDto;
    private DetailsDto detailsDto;
    private static List<ResponseOperationDto> responseOperations;

    @BeforeAll
    void createCredits() {
        CreditDto responseCreditDto = CreditDto.builder()
                .creditId(UUID.randomUUID())
                .creditLimit(new BigDecimal(1000))
                .creditCurrencyCode("EUR")
                .principalDebt(new BigDecimal(120))
                .name("NAME")
                .terminationDate(LocalDate.now().plusYears(1))
                .build();
        responseCredits = List.of(responseCreditDto);

        CreditCardBalanceDto creditCardBalanceDto = CreditCardBalanceDto.builder()
                .cardId(UUID.randomUUID())
                .balance(new BigDecimal(8520))
                .cardNumber("885515")
                .build();

        creditBalanceDto = CreditBalanceDto.builder()
                .creditLimit(new BigDecimal(1000))
                .interestDebt(new BigDecimal(10))
                .principalDebt(new BigDecimal(100))
                .interestRate(new BigDecimal(50))
                .paymentPrincipal(new BigDecimal(20))
                .paymentInterest(new BigDecimal(10))
                .agreementDate(LocalDate.now())
                .paymentDate(LocalDate.now())
                .accountCurrencyCode("RUB")
                .creditCurrencyCode("RUB")
                .accountNumber("5584")
                .name("XVZ")
                .agreementNumber("558844")
                .agreementId(UUID.randomUUID())
                .card(creditCardBalanceDto)
                .build();

        PaymentScheduleDto paymentScheduleDto = PaymentScheduleDto.builder()
                .paymentDate(LocalDate.now())
                .paymentInterest(BigDecimal.valueOf(1337))
                .paymentPrincipal(BigDecimal.valueOf(13372)).build();
        creditScheduleDto = CreditScheduleDto.builder()
                .accountNumber("accountNumber")
                .agreementID(UUID.randomUUID())
                .paymentsSchedule(List.of(paymentScheduleDto))
                .interestDebt(BigDecimal.valueOf(13373))
                .principalDebt(BigDecimal.valueOf(13374)).build();
        detailsDto = DetailsDto.builder()
                .agreementId(UUID.randomUUID())
                .creditCurrencyCode("RUB")
                .principalDebt(new BigDecimal(123))
                .interestDebt(new BigDecimal(321))
                .principal(new BigDecimal(123))
                .interest(new BigDecimal(523)).build();

        responseOperations = List.of(ResponseOperationDto.builder()
                .operationId(UUID.randomUUID())
                .completedAt(LocalDateTime.now())
                .details("test")
                .accountId(UUID.randomUUID())
                .operationType("test")
                .currencyCode("123")
                .type("test")
                .build());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 404, 500})
    @WithMockUser
    @DisplayName("If request for getting operations proceed then status should be redirected")
    void getDetailsOfLastPayments_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditClient.getDetailsOfLastPayments(CLIENT_ID, TEST_CREDIT_ID, 0, 4)).thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credits/{creditId}/history", TEST_CREDIT_ID)
                        .param("clientId", CLIENT_ID.toString())
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(4)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting operations proceed then response body should be redirected")
    void getDetailsOfLastPayments_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditClient.getDetailsOfLastPayments(CLIENT_ID, TEST_CREDIT_ID, 0, 4)).thenReturn(ResponseEntity.ok(responseOperations));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/credits/{creditId}/history", TEST_CREDIT_ID)
                        .param("clientId", CLIENT_ID.toString())
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(4)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(responseOperations), mvcResult.getResponse().getContentAsString());
    }


    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting current credits proceed then status must be redirected")
    void getClientCurrentCredits_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditClient.getClientCreditsWithActiveStatus(clientIdCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credits")).andExpect(status().is(httpStatus));
        verifyClientIdParameter(clientIdCaptor.getValue());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting current credits proceed then response body must be redirected")
    void getClientCurrentCredits_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditClient.getClientCreditsWithActiveStatus(clientIdCaptor.capture()))
                .thenReturn(ResponseEntity.ok(responseCredits));

        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/credits"))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(clientIdCaptor.getValue());
        verifyBody(asJsonString(responseCredits), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit information proceed then status must be redirected")
    void getClientCreditInformation_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditClient.getCreditBalance(any(UUID.class), any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credits/{creditId}", TEST_CREDIT_ID)
                .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit information proceed then response body must be redirected")
    void getClientCreditInformation_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditClient.getCreditBalance(clientIdCaptor.capture(), creditIdCaptor.capture()))
                .thenReturn(ResponseEntity.ok(creditBalanceDto));

        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/credits/{creditId}", TEST_CREDIT_ID)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(clientIdCaptor.getValue());
        verifyCreditIdParameter(creditIdCaptor.getValue());
        verifyBody(asJsonString(creditBalanceDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit payment schedule information proceed then status must be redirected")
    void getClientCreditPaymentScheduleInformation_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditClient.getPaymentSchedule(UUID.fromString(TEST_CLIENT_ID), TEST_CREDIT_ID))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credits/{creditId}/schedule", TEST_CREDIT_ID)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit payment schedule information proceed then response body must be redirected")
    void getClientCreditPaymentScheduleInformation_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditClient.getPaymentSchedule(clientIdCaptor.capture(), creditIdCaptor.capture()))
                .thenReturn(ResponseEntity.ok(creditScheduleDto));

        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/credits/{creditId}/schedule", TEST_CREDIT_ID)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(clientIdCaptor.getValue());
        verifyCreditIdParameter(creditIdCaptor.getValue());
        verifyBody(asJsonString(creditScheduleDto), result.getResponse().getContentAsString());
    }
    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit payment schedule information proceed then status must be redirected")
    void getClientCreditPaymentDetails_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditClient.getDetailsForPayment(UUID.fromString(TEST_CLIENT_ID), TEST_AGREEMENT_ID))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credits/{agreementId}/details", TEST_AGREEMENT_ID)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit payment schedule information proceed then response body must be redirected")
    void getClientCreditPaymentDetails_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditClient.getDetailsForPayment(clientIdCaptor.capture(), agreementIdCaptor.capture()))
                .thenReturn(ResponseEntity.ok(detailsDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/credits/{agreementId}/details", TEST_AGREEMENT_ID)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(clientIdCaptor.getValue());
        verifyAgreementIdParameter(agreementIdCaptor.getValue());
        verifyBody(asJsonString(detailsDto), result.getResponse().getContentAsString());
    }

    private void verifyClientIdParameter(UUID clientIdParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(clientIdParameter)
                .withFailMessage("Client id parameter should not be null")
                .isNotNull();
        softAssertions.assertThat(clientIdParameter.toString())
                .withFailMessage("Client id should be " + TEST_CLIENT_ID + " instead of " + clientIdParameter)
                .isEqualTo(TEST_CLIENT_ID);
        softAssertions.assertAll();
    }

    private void verifyCreditIdParameter(UUID creditIdParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(creditIdParameter)
                .withFailMessage("Credit id parameter should not be null")
                .isNotNull();
        softAssertions.assertThat(creditIdParameter)
                .withFailMessage("Client id should be " + TEST_CREDIT_ID + " instead of " + creditIdParameter)
                .isEqualTo(TEST_CREDIT_ID);
        softAssertions.assertAll();
    }

    private void verifyAgreementIdParameter(UUID agreementIdParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(agreementIdParameter)
                .withFailMessage("Agreement id parameter should not be null")
                .isNotNull();
        softAssertions.assertThat(agreementIdParameter)
                .withFailMessage("Agreement id should be " + TEST_AGREEMENT_ID + " instead of " + agreementIdParameter)
                .isEqualTo(TEST_AGREEMENT_ID);
        softAssertions.assertAll();
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .findAndRegisterModules()
                .writeValueAsString(obj);
    }
}