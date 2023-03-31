package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.ChangeStatusRequestDto;
import by.afinny.apigateway.dto.moneytransfer.ChangeStatusResponseDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentResponseDto;
import by.afinny.apigateway.dto.moneytransfer.IsFavoriteTransferDto;
import by.afinny.apigateway.dto.moneytransfer.RequestRefillBrokerageAccountDto;
import by.afinny.apigateway.dto.moneytransfer.ResponseBrokerageAccountDto;
import by.afinny.apigateway.dto.moneytransfer.TransferDto;
import by.afinny.apigateway.dto.moneytransfer.TransferOrderIdDto;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferStatus;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import by.afinny.apigateway.openfeign.moneytransfer.TransferClient;
import by.afinny.apigateway.service.moneytransfer.impl.TransferServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static by.afinny.apigateway.dto.moneytransfer.constant.TransferStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferClient transferClient;
    @MockBean
    private TransferServiceImpl transferService;

    private final String TRANSFER_ID = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11";
    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final UUID OPERATION_ID = UUID.fromString("f0fca2da-e7d2-11ec-8fea-0242ac120002");

    private TransferDto transferDto;
    private List<TransferTypeName> paymentTypeNameList;
    private IsFavoriteTransferDto isFavoriteTransferDto;
    private TransferOrderIdDto transferOrderIdDto;
    private List<TransferTypeName> typeNameList;
    private ResponseBrokerageAccountDto responseBrokerageAccountDto;
    private RequestRefillBrokerageAccountDto requestRefillBrokerageAccountDto;
    private CreatePaymentResponseDto createPaymentResponseDto;
    public static final String URL_CREATE_PAYMENT_OR_TRANSFER = "/api/v1/payments/new-payment";
    private ChangeStatusRequestDto changeStatusRequestDto;
    private ChangeStatusResponseDto changeStatusResponseDto;

    @BeforeAll
    void setUp() {
        transferDto = TransferDto.builder()
                .transferTypeId(1)
                .purpose("food")
                .payeeId(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"))
                .sum(new BigDecimal("123.5"))
                .isFavorite(true)
                .build();

        isFavoriteTransferDto = IsFavoriteTransferDto.builder()
                .isFavorite(true)
                .build();

        transferOrderIdDto = TransferOrderIdDto.builder()
                .transferOrderId(UUID.randomUUID())
                .build();

        typeNameList = List.of(TransferTypeName.BETWEEN_CARDS,
                TransferTypeName.TO_ANOTHER_CARD,
                TransferTypeName.BY_PHONE_NUMBER,
                TransferTypeName.BY_PAYEE_DETAILS);

        paymentTypeNameList = List.of(TransferTypeName.FAVORITES,
                TransferTypeName.AUTOPAYMENTS,
                TransferTypeName.BANKING_SERVICES,
                TransferTypeName.INFO_SERVISES,
                TransferTypeName.PAYMENT_FOR_SERVICES,
                TransferTypeName.UTILITIES,
                TransferTypeName.OTHER_PAYMENTS);

        requestRefillBrokerageAccountDto = RequestRefillBrokerageAccountDto.builder()
                .brokerageId(UUID.randomUUID())
                .remitterCardNumber("remitterCardNumber")
                .transferTypeId(1)
                .sum(new BigDecimal("10.0000"))
                .build();
        responseBrokerageAccountDto = ResponseBrokerageAccountDto.builder()
                .id(UUID.fromString("27d9f248-f99a-4526-bca7-ed6b5984842f"))
                .build();

        createPaymentResponseDto = CreatePaymentResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .first_name("first_name")
                .last_name("last_name")
                .middle_name("middle_name")
                .created_at(LocalDateTime.now().toString())
                .status(TransferStatus.PERFORMED.name())
                .card_number("0000111100001111")
                .transfer_type_id("1")
                .sum(BigDecimal.valueOf(1000.0).toString())
                .remitter_card_number("0000111100001111")
                .name("name")
                .payee_account_number(UUID.randomUUID().toString())
                .payee_card_number("0000111100001111")
                .mobile_phone("+7999999999")
                .sum_commission(BigDecimal.valueOf(0.0).toString())
                .authorizationCode("1111")
                .currencyExchange(BigDecimal.valueOf(1.0).toString())
                .purpose("purpose")
                .inn("inn")
                .bic("bic")
                .build();

        changeStatusRequestDto = ChangeStatusRequestDto.builder()
                .transferId(UUID.fromString("27d9f248-f99a-4526-bca7-ed6b5984842f"))
                .status(IN_PROGRESS)
                .build();

        changeStatusResponseDto = ChangeStatusResponseDto.builder()
                .transferId(UUID.fromString("27d9f248-f99a-4526-bca7-ed6b5984842f"))
                .status(IN_PROGRESS)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting transfer information proceed then status must be redirected")
    void getFavoriteTransfers_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferClient.getFavoriteTransfers(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(
                        get(TransferController.URL_TRANSFER + TransferController.FAVORITES_URL +
                                TransferController.FAVORITES_TRANSFER_URL, TRANSFER_ID))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting transfer information proceed then response body must be redirected")
    void getFavoriteTransfers_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferClient.getFavoriteTransfers(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity.ok(transferDto));

        //ACT
        MvcResult result = mockMvc.perform(
                        get(TransferController.URL_TRANSFER + TransferController.FAVORITES_URL +
                                TransferController.FAVORITES_TRANSFER_URL, TRANSFER_ID))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(transferDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {204, 401, 400, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for delete draft transfer order proceed then status must be redirected")
    void deleteDraftTransferOrder_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferClient.deleteIdDraftTransfers(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity
                .status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete(TransferController.URL_TRANSFER +
                        TransferController.URL_DELETE_TRANSFER, OPERATION_ID))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If the request to adding/removing to is favorite transfer order continues, the status should be redirected")
    void getTransferOrder_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferClient.getFavoriteTransfers(any(UUID.class), any(TransferOrderIdDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(patch(TransferController.URL_TRANSFER + TransferController.FAVORITES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transferOrderIdDto)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If the request to adding/removing to is favorite transfer order continues, the response body should be redirected")
    void getTransferOrder_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferClient.getFavoriteTransfers(any(UUID.class), any(TransferOrderIdDto.class)))
                .thenReturn(ResponseEntity.ok(isFavoriteTransferDto));

        //ACT
        MvcResult result = mockMvc.perform(patch(TransferController.URL_TRANSFER + TransferController.FAVORITES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transferOrderIdDto)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(isFavoriteTransferDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting transfer type name then status should be redirected")
    void getTransferType_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferClient.getTransferType()).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get(TransferController.URL_TRANSFER + TransferController.URL_TRANSFER_TYPE))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting transfer type name then body should be redirected")
    void getTransferType_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferClient.getTransferType()).thenReturn(ResponseEntity.ok(typeNameList));

        //ACT
        MvcResult result = mockMvc.perform(get(TransferController.URL_TRANSFER + TransferController.URL_TRANSFER_TYPE))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(typeNameList), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting payment type name then status should be redirected")
    void getPaymentType_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferClient.getPaymentType()).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get(TransferController.URL_TRANSFER + TransferController.URL_PAYMENT_TYPE))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting payment type name then body should be redirected")
    void getPaymentType_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferClient.getPaymentType()).thenReturn(ResponseEntity.ok(paymentTypeNameList));

        //ACT
        MvcResult result = mockMvc.perform(get(TransferController.URL_TRANSFER + TransferController.URL_PAYMENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(paymentTypeNameList), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for refill brokerage account then status should be redirected")
    void refillBrokerageAccount_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(transferClient.refillBrokerageAccount(any(UUID.class), any(RequestRefillBrokerageAccountDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(post(TransferController.URL_TRANSFER + TransferController.NEW)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestRefillBrokerageAccountDto)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for refill brokerage account then body should be redirected")
    void refillBrokerageAccount_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferClient.refillBrokerageAccount(any(UUID.class), any(RequestRefillBrokerageAccountDto.class)))
                .thenReturn(ResponseEntity.ok(responseBrokerageAccountDto));

        //ACT
        MvcResult result = mockMvc.perform(post(TransferController.URL_TRANSFER + TransferController.NEW)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestRefillBrokerageAccountDto)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseBrokerageAccountDto), result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for creating payment or transfer was successfully then body should be redirected")
    void createPaymentOrTransfer_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(transferService.createPaymentOrTransfer(any(UUID.class), any(CreatePaymentDto.class)))
                .thenReturn(createPaymentResponseDto);

        //ACT
        MvcResult result = mockMvc.perform(
                        post(URL_CREATE_PAYMENT_OR_TRANSFER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(new CreatePaymentDto())))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(createPaymentResponseDto), result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If createPaymentOrTransfer wasn't successfully then return status INTERNAL SERVER ERROR")
    void createPaymentOrTransfer_ifNotSuccess_then_INTERNAL_SERVER_ERROR() throws Exception {
        //ARRANGE
        when(transferService.createPaymentOrTransfer(any(UUID.class), any(CreatePaymentDto.class)))
                .thenThrow(new RuntimeException());
        //ACT & VERIFY
        mockMvc.perform(
                        post(URL_CREATE_PAYMENT_OR_TRANSFER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(new CreatePaymentDto())))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for changeStatus success then status is ok and return body ")
    void changeStatus_shouldReturnOkAndResponseBody() throws Exception {
        //ARRANGE
        when(transferClient.changeStatus(any(UUID.class)))
                .thenReturn(ResponseEntity.ok(changeStatusResponseDto));

        //ACT
        MvcResult result = mockMvc.perform(patch(TransferController.URL_TRANSFER +
                        TransferController.URL_CHANGE_STATUS, UUID.fromString("27d9f248-f99a-4526-bca7-ed6b5984842f"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(changeStatusResponseDto), result.getResponse().getContentAsString());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }
}