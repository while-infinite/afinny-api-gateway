package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.AccountWithCardInfoDto;
import by.afinny.apigateway.dto.deposit.CardDebitLimitDto;
import by.afinny.apigateway.dto.deposit.CardDto;
import by.afinny.apigateway.dto.deposit.DepositCardStatusDto;
import by.afinny.apigateway.dto.deposit.NewPinCodeDebitCardDto;
import by.afinny.apigateway.dto.deposit.ViewCardDto;
import by.afinny.apigateway.dto.deposit.constant.CardStatus;
import by.afinny.apigateway.dto.deposit.constant.DebitCardStatus;
import by.afinny.apigateway.dto.deposit.constant.DigitalWallet;
import by.afinny.apigateway.dto.deposit.constant.PaymentSystem;

import by.afinny.apigateway.dto.infoservice.constant.CurrencyCode;
import by.afinny.apigateway.openfeign.deposit.CardsClient;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardsClient cardsClient;

    private final String FORMAT_DATE = "yyyy-MM-dd";
    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final UUID CARD_ID = UUID.fromString("f0fca2da-e7d2-11ec-8fea-0242ac120002");

    @Captor
    private ArgumentCaptor<UUID> parameterCaptor;
    @Captor
    private ArgumentCaptor<NewPinCodeDebitCardDto>  newPinCodeCaptor;
    @Captor
    private ArgumentCaptor<UUID>  clientIdCaptor;
    private List<AccountWithCardInfoDto> responseAccountsWithCards;
    private DepositCardStatusDto cardStatusDto;
    private ViewCardDto viewCardDto;
    private CardDebitLimitDto cardDebitLimitDto;
    private NewPinCodeDebitCardDto newPinCodeDebitCardDto;

    @BeforeAll
    void createAccountWithCards() {
        CardDto cardDto = CardDto.builder()
                .cardId(UUID.randomUUID())
                .cardNumber("1")
                .transactionLimit(BigDecimal.ONE)
                .status(CardStatus.ACTIVE)
                .expirationDate(LocalDate.now().plusYears(1))
                .holderName("holderName")
                .digitalWallet(DigitalWallet.APPLEPAY)
                .isDefault(true)
                .cardProductId(1)
                .cardName("cardName")
                .paymentSystem(PaymentSystem.VISA)
                .build();
        List<CardDto> responseCards = List.of(cardDto);

        AccountWithCardInfoDto accountWithCardsDto = AccountWithCardInfoDto.builder()
                .cardId(CARD_ID)
                .cardNumber("1337")
                .expirationDate(LocalDate.now().plusYears(1L))
                .cardName("VISA")
                .paymentSystem(PaymentSystem.MASTERCARD)
                .cardBalance(BigDecimal.ONE)
                .currencyCode(CurrencyCode.RUB).build();
        responseAccountsWithCards = List.of(accountWithCardsDto);

        cardStatusDto = DepositCardStatusDto.builder()
                .cardStatus(DebitCardStatus.BLOCKED)
                .build();

        viewCardDto = ViewCardDto.builder()
                .cardId(CARD_ID)
                .cardNumber("1337")
                .expirationDate(LocalDate.now().plusYears(1L))
                .cardName("VISA")
                .paymentSystem(PaymentSystem.MASTERCARD)
                .cardBalance(BigDecimal.ONE)
                .currencyCode(by.afinny.apigateway.dto.deposit.constant.CurrencyCode.RUB).build();

        cardDebitLimitDto = CardDebitLimitDto.builder()
                .cardNumber("1")
                .transactionLimit(BigDecimal.TEN)
                .build();

        newPinCodeDebitCardDto  = NewPinCodeDebitCardDto.builder()
                .cardNumber("55455")
                .newPin("5855")
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting active products then status should be redirected")
    void getActiveProducts_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(cardsClient.getActiveProducts(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get(CardController.URL_CARDS))
                .andExpect(status().is(httpStatus))
                .andReturn();

        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting active products then body should be redirected")
    void getActiveProducts_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(cardsClient.getActiveProducts(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.ok(responseAccountsWithCards));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get(CardController.URL_CARDS))
                .andExpect(status().isOk())
                .andReturn();

        verifyClientIdParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(responseAccountsWithCards), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for update card status proceed then status should be redirected")
    void changeStatus_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(cardsClient.setNewCardStatus(any(UUID.class), any(DepositCardStatusDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(patch(CardController.URL_CARDS + CardController.URL_ACTIVE_CARDS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cardStatusDto)))
                .andExpect(status().is(httpStatus));
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

    @ParameterizedTest
    @ValueSource( ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting account number by card number proceed then status must be redirected")
    void getAccountByCardId_shouldRedirectStatus (int httpStatus) throws Exception{
        //ARRANGE
        when(cardsClient.getAccountByCardId(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(
                get(CardController.URL_CARDS+CardController.URL_CARD_ID,CARD_ID))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting account number by card number proceed then response body must be redirected")
    void getAccountByCardId_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(cardsClient.getAccountByCardId(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.ok(viewCardDto));

        //ACT
        MvcResult result = mockMvc.perform(
                get(CardController.URL_CARDS+CardController.URL_CARD_ID, CARD_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(viewCardDto),result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for change debit card limit  then body should be redirected")
    void changeDebitCardLimit_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(cardsClient.setDebitCardLimit(any(UUID.class), any(CardDebitLimitDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        //ACT & VERIFY
        mockMvc.perform(patch(CardController.URL_CARDS + CardController.URL_LIMIT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cardDebitLimitDto)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for update debit card limit proceed then status should be redirected")
    void changeDebitCardLimit_shouldRedirectLimit(int httpStatus) throws Exception {
        //ARRANGE
        when(cardsClient.setDebitCardLimit(any(UUID.class), any(CardDebitLimitDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(patch(CardController.URL_CARDS + CardController.URL_LIMIT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cardDebitLimitDto)))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for deleting debit card proceed then status should be redirected")
    void deleteDebitCard_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(cardsClient.deleteDebitCard(any(UUID.class), any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete(CardController.URL_CARDS +
                        CardController.URL_CARD_ID, CARD_ID))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if change pin-code debit card then status should be redirected")
    void changePinCodeDebitCard_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(cardsClient.changePinCodeDebitCard(any(UUID.class), any(NewPinCodeDebitCardDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(post(
                        CardController.URL_CARDS + CardController.URL_CARD_PIN_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newPinCodeDebitCardDto)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if change pin-code debit card then response body should be redirected")
    void changePinCodeDebitCard_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(cardsClient.changePinCodeDebitCard(clientIdCaptor.capture(), newPinCodeCaptor.capture()))
                .thenReturn(ResponseEntity.ok().build());

        //ACT
        mockMvc.perform(post(
                        CardController.URL_CARDS + CardController.URL_CARD_PIN_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newPinCodeDebitCardDto)))
                .andExpect(status().isOk());

        //VERIFY
        verifyNewPinCodeDebitCardDto(newPinCodeCaptor.getValue().toString());
    }

    private void verifyNewPinCodeDebitCardDto(String parameter) {
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(parameter)
                    .withFailMessage("NewPinCodeDebitCardDto parameter must not be null")
                    .isNotNull();
            softAssertions.assertThat(parameter)
                    .withFailMessage("newPinCodeDebitCardDto  must be " + newPinCodeDebitCardDto + " instead of " + parameter)
                    .isEqualTo(newPinCodeDebitCardDto.toString());
        });
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat(FORMAT_DATE))
                .registerModule(new JavaTimeModule()).writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}