package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.CardInfoDto;
import by.afinny.apigateway.dto.credit.CreditCardDto;
import by.afinny.apigateway.dto.credit.CreditCardLimitDto;
import by.afinny.apigateway.dto.credit.CreditCardPinCodeDto;
import by.afinny.apigateway.dto.credit.RequestCardStatusDto;
import by.afinny.apigateway.dto.credit.constant.CreditCardStatus;
import by.afinny.apigateway.dto.credit.constant.PaymentSystem;
import by.afinny.apigateway.openfeign.credit.CreditCardClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditCardController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CreditCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditCardClient creditCardClient;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final UUID CREDIT_CARD_ID = UUID.randomUUID();

    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;
    @Captor
    private ArgumentCaptor<CreditCardPinCodeDto> creditCardPinCodeDtoArgumentCaptor;
    private List<CreditCardDto> responseCreditCards;
    private RequestCardStatusDto requestCardStatus;
    private CardInfoDto cardInfoDto;
    private CreditCardLimitDto creditCardLimitDto;
    private CreditCardPinCodeDto creditCardPinCodeDto;

    @BeforeAll
    void createCreditCards() {
        CreditCardDto creditCardDto = CreditCardDto.builder()
                .id(CREDIT_CARD_ID)
                .accountNumber("123")
                .cardNumber("1111222233334444")
                .balance(BigDecimal.valueOf(5000.00))
                .currencyCode("810")
                .build();

        responseCreditCards = List.of(creditCardDto);

        requestCardStatus = RequestCardStatusDto.builder()
                .cardNumber("1234567890")
                .cardStatus(CreditCardStatus.ACTIVE)
                .build();

        cardInfoDto = CardInfoDto.builder()
                .creditId(UUID.randomUUID().toString())
                .accountNumber("123")
                .balance(BigDecimal.valueOf(5000.00))
                .holderName("holder_name")
                .expirationDate(LocalDate.of(2024, 1, 2).toString())
                .paymentSystem(PaymentSystem.AMERICAN_EXPRESS)
                .status(CreditCardStatus.BLOCKED)
                .transactionLimit(new BigDecimal(700))
                .name("TEST")
                .principalDebt(BigDecimal.valueOf(1))
                .creditLimit(BigDecimal.valueOf(150000))
                .creditCurrencyCode("182")
                .terminationDate(LocalDate.of(2024, 1, 2).toString())
                .build();

        creditCardLimitDto = CreditCardLimitDto.builder()
                .cardNumber("1234567890")
                .transactionLimit(new BigDecimal(1000))
                .build();

        creditCardPinCodeDto = CreditCardPinCodeDto.builder()
                .cardNumber("123456")
                .newPin("0000")
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for update card status proceed then status should be redirected")
    void changeStatus_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditCardClient.setNewCardStatus(any(UUID.class), any(RequestCardStatusDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/credit-cards/active-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestCardStatus))
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit cards proceed then status must be redirected")
    void getCreditCards_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditCardClient.getCreditCards(any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credit-cards")).andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit cards proceed then response body must be redirected")
    void getCreditCards_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditCardClient.getCreditCards(clientIdCaptor.capture()))
                .thenReturn(ResponseEntity.ok(responseCreditCards));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/credit-cards"))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(clientIdCaptor.getValue());
        verifyBody(asJsonString(responseCreditCards), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting card balance information proceed then status must be redirected")
    void getCardInformation_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditCardClient.getCardInformation(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(
                        get(CreditCardController.URL_CARDS_CLIENT_ID)
                                .param(CreditCardController.PARAM_CARD_ID, CREDIT_CARD_ID.toString())
                                .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting card balance information proceed then response body must be redirected")
    void getCardInformation_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditCardClient.getCardInformation(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.ok(cardInfoDto));

        //ACT
        MvcResult result = mockMvc.perform(
                        get(CreditCardController.URL_CARDS_CLIENT_ID)
                                .param(CreditCardController.PARAM_CARD_ID, CREDIT_CARD_ID.toString())
                                .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(cardInfoDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for setting credit card limit proceed then status should be redirected")
    void setCreditCardLimit_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditCardClient.setCreditCardLimit(any(UUID.class), any(CreditCardLimitDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(patch(CreditCardController.LIMIT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(creditCardLimitDto))
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for deleting credit card proceed then status should be redirected")
    void deleteCreditCard_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditCardClient.deleteCreditCard(any(UUID.class), any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete(CreditCardController.DELETE_URL)
                        .param(CreditCardController.PARAM_CARD_ID, String.valueOf(CREDIT_CARD_ID))
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if change pin-code credit card then status should be redirected")
    void changeCardPinCode_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditCardClient.changeCardPinCode(any(UUID.class), any(CreditCardPinCodeDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(post(
                        CreditCardController.URL_CARDS + CreditCardController.URL_CARD_PIN_CODE)
                        .param("clientId", TEST_CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(creditCardPinCodeDto))
                )
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if change pin-code credit card then response body should be redirected")
    void changeCardPinCode_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(creditCardClient.changeCardPinCode(clientIdCaptor.capture(), creditCardPinCodeDtoArgumentCaptor.capture()))
                .thenReturn(ResponseEntity.ok().build());

        //ACT
        mockMvc.perform(post(
                        CreditCardController.URL_CARDS + CreditCardController.URL_CARD_PIN_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(creditCardPinCodeDto))
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk());

        //VERIFY
        verifyClientIdParameter(clientIdCaptor.getValue());
        verifyCreditCardPinCodeDTO(creditCardPinCodeDtoArgumentCaptor.getValue().toString());
    }

    private void verifyCreditCardPinCodeDTO(String parameter) {
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(parameter)
                    .withFailMessage("CreditCardPinCodeDto parameter must not be null")
                    .isNotNull();
            softAssertions.assertThat(parameter)
                    .withFailMessage("newPinCodeDebitCardDto  must be " + creditCardPinCodeDto + " instead of " + parameter)
                    .isEqualTo(creditCardPinCodeDto.toString());
        });
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }

    private void verifyClientIdParameter(UUID clientIdParameter) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(clientIdParameter)
                    .withFailMessage("Client id parameter should not be null")
                    .isNotNull();
            softAssertions.assertThat(clientIdParameter.toString())
                    .withFailMessage("Client id should be " + TEST_CLIENT_ID + " instead of " + clientIdParameter)
                    .isEqualTo(TEST_CLIENT_ID);
            softAssertions.assertAll();
        });
    }
}