package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CreditCardStatementDto;
import by.afinny.apigateway.dto.moneytransfer.DebitCardStatementDto;
import by.afinny.apigateway.dto.moneytransfer.DetailsHistoryDto;
import by.afinny.apigateway.dto.moneytransfer.TransferOrderHistoryDto;
import by.afinny.apigateway.dto.moneytransfer.constant.CurrencyCode;
import by.afinny.apigateway.dto.moneytransfer.constant.PayeeType;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferStatus;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import by.afinny.apigateway.openfeign.moneytransfer.TransferHistoryClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferHistoryController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class TransferHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransferHistoryClient transferHistoryClient;

    private List<TransferOrderHistoryDto> transferOrderHistoryDto = new ArrayList<>();

    private List<DebitCardStatementDto> debitCardStatementDto = new ArrayList<>();
    private DetailsHistoryDto detailsHistoryDto;
    private List<CreditCardStatementDto> creditCardStatementDto = new ArrayList<>();
    private final UUID TRANSFER_ID = UUID.randomUUID();
    private final UUID CARD_ID = UUID.randomUUID();
    private final UUID CLIENT_ID = UUID.randomUUID();
    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final String FORMAT_DATE = "yyyy-MM-dd";
    private final Integer pageNumber = 1;
    private final Integer pageSize = 5;
    private final String from = "2023-03-27 00:00:00";
    private final String to = "2023-03-28 00:00:00";
    private final String REMITTER_CARD_NUMBER = "11111111";
    private final String PAYEE_NAME = "XVZ";

    @BeforeAll
    void setUp() {
        TransferOrderHistoryDto.builder()
                .transferOrderId(TRANSFER_ID)
                .completedAt(LocalDateTime.now())
                .sum(new BigDecimal("5.0"))
                .payeeId(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"))
                .name(PAYEE_NAME)
                .transferStatus(TransferStatus.DRAFT)
                .transferTypeName(TransferTypeName.BETWEEN_CARDS)
                .purpose("XV")
                .currencyCode(CurrencyCode.RUB)
                .remitterCardNumber(REMITTER_CARD_NUMBER)
                .createdAt(LocalDateTime.now())
                .build();

        transferOrderHistoryDto.add(TransferOrderHistoryDto.builder()
                .transferOrderId(TRANSFER_ID)
                .completedAt(LocalDateTime.now())
                .sum(new BigDecimal("5.0"))
                .payeeId(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"))
                .name(PAYEE_NAME)
                .transferStatus(TransferStatus.DRAFT)
                .transferTypeName(TransferTypeName.BETWEEN_CARDS)
                .purpose("XV")
                .currencyCode(CurrencyCode.RUB)
                .remitterCardNumber(REMITTER_CARD_NUMBER)
                .createdAt(LocalDateTime.now())
                .build());

        creditCardStatementDto.add(CreditCardStatementDto.builder()
                .transferOrderId(UUID.randomUUID())
                .purpose("purpose")
                .typeName(TransferTypeName.BETWEEN_CARDS)
                .currencyCode(CurrencyCode.RUB)
                .sum(BigDecimal.TEN)
                .payeeId(UUID.randomUUID())
                .completedAt(LocalDateTime.now())
                .build());

        debitCardStatementDto.add(DebitCardStatementDto.builder()
                .transferOrderId(UUID.randomUUID())
                .purpose("purpose")
                .typeName(TransferTypeName.BETWEEN_CARDS)
                .currencyCode(CurrencyCode.RUB)
                .sum(BigDecimal.TEN)
                .payeeId(UUID.randomUUID())
                .completedAt(LocalDateTime.now())
                .build());

        detailsHistoryDto = DetailsHistoryDto.builder()
                .createdAt(LocalDateTime.now())
                .remitterCardNumber("1234567898765432")
                .sumCommission(BigDecimal.ONE)
                .currencyExchange(BigDecimal.ONE)
                .payeeType(PayeeType.INDIVIDUALS)
                .name("RUB")
                .inn("123456789009")
                .bic("123456789")
                .payeeAccountNumber("a0eebc999")
                .payeeCardNumber("9876543210")
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting transfer order history, then status should be redirected")
    void transferOrderHistory_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferHistoryClient.getTransferOrderHistory(any()))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get(TransferHistoryController.HISTORY_URL)
                        .param("pageNumber", pageNumber.toString())
                        .param("pageSize", pageSize.toString()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting transfer order history then response body should be redirected")
    void transferOrderHistory_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferHistoryClient.getTransferOrderHistory(any()))
                .thenReturn(ResponseEntity.ok(transferOrderHistoryDto));

        //ACT
        MvcResult result = mockMvc.perform(get(TransferHistoryController.HISTORY_URL)
                        .param("pageNumber", pageNumber.toString())
                        .param("pageSize", pageSize.toString()))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(transferOrderHistoryDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting transfer order history, then status should be redirected")
    void viewDebitCardStatement_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferHistoryClient.getViewDebitCardStatement(UUID.fromString(TEST_CLIENT_ID),TRANSFER_ID, from, to, pageNumber, pageSize))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get(TransferHistoryController.HISTORY_URL
                        + TransferHistoryController.DEPOSIT_URL, TRANSFER_ID)
                        .param(TransferHistoryController.FROM_PARAM, from)
                        .param(TransferHistoryController.TO_PARAM, to)
                        .param(TransferHistoryController.PAGE_NUMBER_PARAM, pageNumber.toString())
                        .param(TransferHistoryController.PAGE_SIZE_PARAM, pageSize.toString()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting transfer order history then response body should be redirected")
    void viewDebitCardStatement_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferHistoryClient.getViewDebitCardStatement(UUID.fromString(TEST_CLIENT_ID),TRANSFER_ID, from, to, pageNumber, pageSize))
                .thenReturn(ResponseEntity.ok(debitCardStatementDto));

        //ACT
        MvcResult result = mockMvc.perform(get(TransferHistoryController.HISTORY_URL
                        + TransferHistoryController.DEPOSIT_URL, TRANSFER_ID)
                        .param(TransferHistoryController.FROM_PARAM, from)
                        .param(TransferHistoryController.TO_PARAM, to)
                        .param(TransferHistoryController.PAGE_NUMBER_PARAM, pageNumber.toString())
                        .param(TransferHistoryController.PAGE_SIZE_PARAM, pageSize.toString()))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyBody(asJsonString(debitCardStatementDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting thr transfer order details history proceed then status should be redirected")
    void getDetailsHistory_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferHistoryClient.getDetailsHistory(UUID.fromString(TEST_CLIENT_ID),TRANSFER_ID)).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get(TransferHistoryController.HISTORY_URL
                        + TransferHistoryController.DETAILS_URL, TRANSFER_ID.toString())
                        .param("transferOrderId", TRANSFER_ID.toString()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting thr transfer order details history proceed then body should be redirected")
    void getDetailsHistory_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferHistoryClient.getDetailsHistory(UUID.fromString(TEST_CLIENT_ID),TRANSFER_ID)).thenReturn(ResponseEntity.ok(detailsHistoryDto));

        //ACT
        MvcResult result = mockMvc.perform(get(TransferHistoryController.HISTORY_URL
                        + TransferHistoryController.DETAILS_URL, TRANSFER_ID.toString())
                        .param("transferOrderId", TRANSFER_ID.toString()))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(detailsHistoryDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit card statement, then status should be redirected")
    void getCreditCardStatement_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferHistoryClient.getCreditCardStatement(UUID.fromString(TEST_CLIENT_ID), CARD_ID, from, to, pageNumber, pageSize))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get(TransferHistoryController.HISTORY_URL + TransferHistoryController.CREDIT_URL,CARD_ID)
                        .param(TransferHistoryController.CLIENT_ID_PARAM,CLIENT_ID.toString())
                        .param(TransferHistoryController.FROM_PARAM, from)
                        .param(TransferHistoryController.TO_PARAM, to)
                        .param(TransferHistoryController.PAGE_NUMBER_PARAM, pageNumber.toString())
                        .param(TransferHistoryController.PAGE_SIZE_PARAM, pageSize.toString()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit card statement then response body should be redirected")
    void getCreditCardStatement_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferHistoryClient.getCreditCardStatement(UUID.fromString(TEST_CLIENT_ID), CARD_ID, from, to, pageNumber, pageSize))
                .thenReturn(ResponseEntity.ok(creditCardStatementDto));

        //ACT
        MvcResult result = mockMvc.perform(get(TransferHistoryController.HISTORY_URL + TransferHistoryController.CREDIT_URL, CARD_ID)
                        .param(TransferHistoryController.CLIENT_ID_PARAM,CLIENT_ID.toString())
                        .param(TransferHistoryController.FROM_PARAM, from)
                        .param(TransferHistoryController.TO_PARAM, to)
                        .param(TransferHistoryController.PAGE_NUMBER_PARAM, pageNumber.toString())
                        .param(TransferHistoryController.PAGE_SIZE_PARAM, pageSize.toString()))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(creditCardStatementDto), result.getResponse().getContentAsString());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat(FORMAT_DATE))
                .registerModule(new JavaTimeModule()).writeValueAsString(obj);
    }
}