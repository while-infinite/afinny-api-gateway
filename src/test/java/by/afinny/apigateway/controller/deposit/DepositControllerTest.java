package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.ActiveDepositDto;
import by.afinny.apigateway.dto.deposit.DepositDto;
import by.afinny.apigateway.dto.deposit.RequestAutoRenewalDto;
import by.afinny.apigateway.dto.deposit.RequestNewDepositDto;
import by.afinny.apigateway.dto.deposit.WithdrawDepositDto;
import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
import by.afinny.apigateway.dto.deposit.constant.SchemaName;
import by.afinny.apigateway.openfeign.deposit.DepositClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepositController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DepositControllerTest {

    @MockBean
    private DepositClient depositClient;

    @Autowired
    private MockMvc mockMvc;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final UUID AGREEMENT_ID = UUID.randomUUID();
    private final UUID CARD_ID = UUID.randomUUID();
    private final String FORMAT_DATE = "yyyy-MM-dd";

    @Captor
    private ArgumentCaptor<WithdrawDepositDto> dtoCaptor;
    @Captor
    private ArgumentCaptor<UUID> idCaptor;
    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;
    @Captor
    private ArgumentCaptor<UUID> parameterCaptor;
    private WithdrawDepositDto withdrawDepositDto;
    private DepositDto depositDto;
    private List<ActiveDepositDto> responseListDeposits;
    private RequestAutoRenewalDto requestAutoRenewalDto;

    @BeforeAll
    void setUp() {
        withdrawDepositDto = WithdrawDepositDto.builder()
                .cardNumber("1")
                .build();

        depositDto = DepositDto.builder()
                .cardNumber("1234567890123456")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(12L))
                .interestRate(new BigDecimal(10))
                .currentBalance(new BigDecimal(10))
                .autoRenewal(true)
                .name("name")
                .currencyCode(CurrencyCode.RUB)
                .schemaName(SchemaName.FIXED)
                .isCapitalization(true)
                .isRevocable(true).build();

        ActiveDepositDto activeDepositDto = ActiveDepositDto.builder()
                .agreementId(UUID.randomUUID())
                .currentBalance(new BigDecimal("1000000.00"))
                .productName("product name")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusYears(1))
                .currencyCode(CurrencyCode.RUB)
                .cardNumber("1111222233334444").
                build();
        responseListDeposits = List.of(activeDepositDto);

        requestAutoRenewalDto = RequestAutoRenewalDto.builder()
                .autoRenewal(true)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for creating order proceed then status should be redirected")
    void getAgreement_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(depositClient.earlyWithdrawalDeposit(any(UUID.class), any(UUID.class), any(WithdrawDepositDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/deposits/{agreementId}/revocation", AGREEMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(withdrawDepositDto)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting credit orders then body should be redirected")
    void getAgreement_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(depositClient.earlyWithdrawalDeposit(clientIdCaptor.capture(), idCaptor.capture(), dtoCaptor.capture()))
                .thenReturn(ResponseEntity.ok().build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/deposits/{agreementId}/revocation", AGREEMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(withdrawDepositDto)))
                .andExpect(status().isOk())
                .andReturn();
        verifyParameter(AGREEMENT_ID.toString(), idCaptor.getValue().toString());
        verifyParameter(withdrawDepositDto.toString(), dtoCaptor.getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting deposit proceed then status should be redirected")
    void getDeposit_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(depositClient.getDeposit(UUID.fromString(TEST_CLIENT_ID), AGREEMENT_ID, CARD_ID)).thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/deposits/{agreementId}", AGREEMENT_ID.toString())
                        .param("cardId", CARD_ID.toString()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting deposit proceed then response body should be redirected")
    void getDeposit_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(depositClient.getDeposit(UUID.fromString(TEST_CLIENT_ID), AGREEMENT_ID, CARD_ID)).thenReturn(ResponseEntity.ok(depositDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/deposits/{agreementId}", AGREEMENT_ID.toString())
                        .param("cardId", CARD_ID.toString()))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(depositDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting active deposits proceed then status should be redirected")
    void getActiveDeposits_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(depositClient.getActiveDeposits(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get(DepositController.URL_DEPOSITS)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));

        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting active deposits proceed then response body should be redirected")
    void getActiveDeposits_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(depositClient.getActiveDeposits(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.ok(responseListDeposits));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get(DepositController.URL_DEPOSITS)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();

        verifyClientIdParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(responseListDeposits), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if update auto renewal then status should be redirected")
    void updateAutoRenewal_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(depositClient.updateAutoRenewal(any(UUID.class), any(UUID.class), any(RequestAutoRenewalDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch(DepositController.URL_DEPOSITS + DepositController.URL_DEPOSITS_AUTO_RENEWAL, AGREEMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestAutoRenewalDto)))
                .andExpect(status().is(httpStatus));
    }


    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat(FORMAT_DATE))
                .registerModule(new JavaTimeModule()).writeValueAsString(obj);
    }

    private void verifyParameter(String expectedParameter, String actualParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualParameter)
                .withFailMessage("Parameter parameter must not be null")
                .isNotNull();
        softAssertions.assertThat(actualParameter)
                .withFailMessage("Parameter must be " + expectedParameter + " instead of " + actualParameter)
                .isEqualTo(expectedParameter);
        softAssertions.assertAll();
    }

    private void verifyClientIdParameter(UUID clientIdParameter) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(clientIdParameter)
                    .withFailMessage("Client id parameter must not be null")
                    .isNotNull();
            softAssertions.assertThat(clientIdParameter.toString())
                    .withFailMessage("Client id must be " + TEST_CLIENT_ID + " instead of " + clientIdParameter)
                    .isEqualTo(TEST_CLIENT_ID);
        });
    }
}