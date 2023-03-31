package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.RequestCreditOrderDto;
import by.afinny.apigateway.dto.credit.ResponseCreditOrderDto;
import by.afinny.apigateway.dto.credit.constant.CreditOrderStatus;
import by.afinny.apigateway.openfeign.credit.CreditOrderClient;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditOrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CreditOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreditOrderClient creditOrderClient;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final UUID ORDER_ID= UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19");
    private final Integer TEST_PRODUCT_ID = 1;

    @Captor ArgumentCaptor<UUID> parameterCaptor;
    private RequestCreditOrderDto requestCreditOrderDto;
    private ResponseCreditOrderDto responseCreditOrderDto;
    private List<ResponseCreditOrderDto> responseList;

    @BeforeAll
    void createRequestCreditOrderDto() {
        requestCreditOrderDto = RequestCreditOrderDto.builder()
                .productId(TEST_PRODUCT_ID)
                .amount(new BigDecimal(1240))
                .periodMonths(36)
                .monthlyIncome(new BigDecimal(30))
                .monthlyExpenditure(new BigDecimal(45))
                .employerIdentificationNumber("xertqw-124-sqafn-02").build();
        responseCreditOrderDto = ResponseCreditOrderDto.builder()
                .id(UUID.randomUUID())
                .productName("FFF")
                .productId(TEST_PRODUCT_ID)
                .status(CreditOrderStatus.PENDING)
                .amount(requestCreditOrderDto.getAmount())
                .periodMonths(requestCreditOrderDto.getPeriodMonths()).build();
        responseList = List.of(responseCreditOrderDto);
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for creating order proceed then status should be redirected")
    void createOrder_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditOrderClient.createOrder(parameterCaptor.capture(), any(RequestCreditOrderDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/credit-orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestCreditOrderDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for creating order proceed then body should be redirected")
    void createOrder_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(creditOrderClient.createOrder(parameterCaptor.capture(), any(RequestCreditOrderDto.class)))
                .thenReturn(ResponseEntity.ok(responseCreditOrderDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(post("/api/v1/credit-orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestCreditOrderDto)))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(responseCreditOrderDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit orders proceed then status should be redirected")
    void getClientCreditOrders_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditOrderClient.getCreditOrders(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credit-orders")
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting credit orders then body should be redirected")
    void getClientCreditOrders_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(creditOrderClient.getCreditOrders(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.ok(responseList));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/credit-orders")
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(responseList), result.getResponse().getContentAsString());
    }
    @ParameterizedTest
    @ValueSource(ints = {204, 401, 400, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for delete credit order proceed then status must be redirected")
    void deleteCreditOrder_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditOrderClient.deleteCreditOrder(any(UUID.class), any(UUID.class))).thenReturn(ResponseEntity
                .status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(delete(CreditOrderController.URL_CREDIT_ORDERS
                        + CreditOrderController.PARAM_CREDIT_ORDER_ID, ORDER_ID)
                        .param("clientId", TEST_CLIENT_ID))
                .andExpect(status().is(httpStatus));
    }

    private void verifyClientIdParameter(UUID clientIdParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(clientIdParameter)
                .withFailMessage("client id parameter must not be null")
                .isNotNull();
        softAssertions.assertThat(clientIdParameter.toString())
                .withFailMessage("client id must be " + TEST_CLIENT_ID + " instead of " + clientIdParameter)
                .isEqualTo(TEST_CLIENT_ID);
        softAssertions.assertAll();
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

}