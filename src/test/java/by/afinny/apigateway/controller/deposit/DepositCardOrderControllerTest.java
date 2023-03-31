package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.RequestDepositCardOrderDto;
import by.afinny.apigateway.openfeign.deposit.CardOrderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

import java.util.UUID;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepositCardOrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DepositCardOrderControllerTest {

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardOrderClient cardOrderClient;

    @Captor
    private ArgumentCaptor<UUID> parameterCaptor;
    private RequestDepositCardOrderDto requestDepositCardOrderDto;

    @BeforeAll
    void createAccountWithCards() {
        requestDepositCardOrderDto = RequestDepositCardOrderDto.builder()
                .productId(1).build();

    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting active products then status should be redirected")
    void getActiveProducts_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(cardOrderClient.orderNewCard(parameterCaptor.capture(), any(RequestDepositCardOrderDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(
                post(DepositCardOrderController.URL_CARD_ORDERS + DepositCardOrderController.URL_NEW)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDepositCardOrderDto)))
                .andExpect(status().is(httpStatus))
                .andReturn();

        verifyClientIdParameter(parameterCaptor.getValue());
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

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(obj);
    }
}
