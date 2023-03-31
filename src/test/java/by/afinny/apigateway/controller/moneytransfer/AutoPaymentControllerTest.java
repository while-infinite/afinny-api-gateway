package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.AutoPaymentDto;
import by.afinny.apigateway.dto.moneytransfer.AutoPaymentsDto;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferPeriodicity;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import by.afinny.apigateway.openfeign.moneytransfer.AutoPaymentClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AutoPaymentController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class AutoPaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AutoPaymentClient autoPaymentClient;

    private AutoPaymentDto autoPaymentDto;
    private List<AutoPaymentsDto> autoPaymentsDtoList = new ArrayList<>();
    private final UUID TRANSFER_ID = UUID.randomUUID();
    private final String CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";


    @BeforeAll
    void setUp() {
        autoPaymentDto = AutoPaymentDto.builder()
                .periodicity(TransferPeriodicity.MONTHLY)
                .startDate(LocalDateTime.now()).build();

        AutoPaymentsDto autoPaymentsDto = AutoPaymentsDto.builder()
                .transferOrderId(UUID.randomUUID())
                .typeName(TransferTypeName.BETWEEN_CARDS).build();
        autoPaymentsDtoList.add(autoPaymentsDto);
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser
    @DisplayName("if update auto payment then status should be redirected")
    void updateAutoPayment_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        Mockito.when(autoPaymentClient.updateAutoPayment(any(UUID.class), any(AutoPaymentDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(patch(AutoPaymentController.URL_AUTO_PAYMENT)
                        .param("transferId", TRANSFER_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(autoPaymentDto)))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("if view Auto Payments then status should be redirected")
    void viewAutoPayments_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(autoPaymentClient.viewAutoPayments(any(UUID.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get(AutoPaymentController.URL_AUTO_PAYMENT))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("if view Auto Payments then response body should be redirected")
    void viewAutoPayments_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(autoPaymentClient.viewAutoPayments(any(UUID.class)))
                .thenReturn(ResponseEntity.ok(autoPaymentsDtoList));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get(AutoPaymentController.URL_AUTO_PAYMENT))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(autoPaymentsDtoList), result.getResponse().getContentAsString());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
    }
}