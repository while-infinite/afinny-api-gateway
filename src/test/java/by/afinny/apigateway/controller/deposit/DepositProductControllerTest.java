package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.ProductDto;
import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
import by.afinny.apigateway.openfeign.deposit.DepositProductClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepositProductController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DepositProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepositProductClient depositProductClient;
    private List<ProductDto> responseListProducts;

    @BeforeAll
    void createProducts() {
        ProductDto productDto = ProductDto.builder()
                .name("Product #1")
                .id(1)
                .minInterestRate(new BigDecimal(10))
                .maxInterestRate(new BigDecimal(12))
                .interestRateEarly(new BigDecimal(9))
                .currencyCode(CurrencyCode.RUB)
                .isRevocable(true)
                .schemaName("FIXED")
                .isCapitalization(true)
                .minDurationMonths(10)
                .maxDurationMonths(20)
                .amountMin(new BigDecimal(1))
                .amountMax(new BigDecimal(100000))
                .build();
        responseListProducts = List.of(productDto);
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser
    @DisplayName("if request for getting active deposit products proceed then status should be redirected")
    void getActiveDepositProducts_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(depositProductClient.getActiveDepositProducts()).thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/deposit-products")).andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("if request for getting active deposit products proceed then response body should be redirected")
    void getActiveDepositProducts_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(depositProductClient.getActiveDepositProducts()).thenReturn(ResponseEntity.ok(responseListProducts));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/deposit-products"))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(responseListProducts), result.getResponse().getContentAsString());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
    }
}