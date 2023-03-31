package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.CardProductDto;
import by.afinny.apigateway.exception.handler.ExceptionHandlerController;
import by.afinny.apigateway.openfeign.deposit.CardProductsClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardProductsController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CardProductsControllerTest {

    @MockBean
    private CardProductsClient cardProductsClient;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final String FORMAT_DATE = "yyyy-MM-dd";

    private MockMvc mockMvc;
    private List<CardProductDto> cardProducts;

    @BeforeAll
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(new CardProductsController(cardProductsClient))
                .setControllerAdvice(new ExceptionHandlerController())
                .build();

        cardProducts = List.of(
                CardProductDto.builder()
                        .id(1)
                        .cardName("TEST")
                        .paymentSystem("VISA")
                        .coBrand("AEROFLOT")
                        .isVirtual(Boolean.FALSE)
                        .premiumStatus("CLASSIC")
                        .servicePrice(BigDecimal.valueOf(0))
                        .productPrice(BigDecimal.valueOf(0))
                        .currencyCode("RUB")
                        .isActive(Boolean.TRUE)
                        .cardDuration(5).build());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting all card products then status should be redirected")
    void getAllCardProducts_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(cardProductsClient.getAllCardProducts()).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/cards-products"))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting all card products then body should be redirected")
    void getAllCardProducts_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(cardProductsClient.getAllCardProducts()).thenReturn(ResponseEntity.ok(cardProducts));

        //ACT
        MvcResult result = mockMvc.perform(get("/api/v1/cards-products"))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(cardProducts), result.getResponse().getContentAsString());
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