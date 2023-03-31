package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.RequestNewDepositDto;
import by.afinny.apigateway.openfeign.deposit.DepositOrderClient;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepositOrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DepositOrderControllerTest {

    @MockBean
    private DepositOrderClient depositOrderClient;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<RequestNewDepositDto> captor;

    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;

    private final String FORMAT_DATE = "yyyy-MM-dd";

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";

    private RequestNewDepositDto requestNewDepositDto;

    @BeforeAll
    void setUp() {
        requestNewDepositDto = RequestNewDepositDto.builder()
                .productId(1)
                .initialAmount(new BigDecimal(10))
                .cardNumber("1")
                .autoRenewal(true)
                .interestRate(new BigDecimal(10))
                .durationMonth(10)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if create new deposit then status should be redirected")
    void createNewDeposit_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(depositOrderClient.createNewDeposit(any(UUID.class), any(RequestNewDepositDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post(
                DepositOrderController.DEPOSIT_ORDER_URL + DepositOrderController.NEW_DEPOSIT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestNewDepositDto)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if create new deposit then response body should be redirected")
    void createNewDeposit_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(depositOrderClient.createNewDeposit(clientIdCaptor.capture(), captor.capture()))
                .thenReturn(ResponseEntity.ok().build());
        //ACT & VERIFY
        mockMvc.perform(post(
                DepositOrderController.DEPOSIT_ORDER_URL + DepositOrderController.NEW_DEPOSIT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestNewDepositDto)))
                        .andExpect(status().isOk());
        verifyRequestNewDepositDto(captor.getValue().toString());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat(FORMAT_DATE))
                .registerModule(new JavaTimeModule()).writeValueAsString(obj);
    }

    private void verifyRequestNewDepositDto(String parameter) {
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(parameter)
                    .withFailMessage("RequestNewDepositDto parameter must not be null")
                    .isNotNull();
            softAssertions.assertThat(parameter)
                    .withFailMessage("withdrawDepositDto must be " + requestNewDepositDto + " instead of " + parameter)
                    .isEqualTo(requestNewDepositDto.toString());
        });
    }
}