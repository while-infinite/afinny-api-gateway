package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.ProductDto;
import by.afinny.apigateway.dto.credit.constant.CalculationMode;
import by.afinny.apigateway.openfeign.credit.CreditProductClient;
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

@WebMvcTest(CreditProductController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CreditProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreditProductClient creditProductClient;
    private List<ProductDto> responseProducts;

    @BeforeAll
    void createProducts() {
        ProductDto responseProductDto = ProductDto.builder()
                .id(1)
                .name("mortgage")
                .minSum(new BigDecimal(400))
                .maxSum(new BigDecimal(1000))
                .currencyCode("RUB")
                .minInterestRate(new BigDecimal(20))
                .maxInterestRate(new BigDecimal(170))
                .needGuarantees(true)
                .deliveryInCash(false)
                .earlyRepayment(true)
                .needIncomeDetails(true)
                .minPeriodMonths(60)
                .maxPeriodMonths(480)
                .isActive(true)
                .details("has benefits for bank employees")
                .calculationMode(CalculationMode.DIFFERENTIATED)
                .gracePeriodMonths(12)
                .rateIsAdjustable(true)
                .rateBase("9")
                .rateFixPart(new BigDecimal(25))
                .increasedRate(new BigDecimal(3)).build();

        responseProducts = List.of(responseProductDto);
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser
    @DisplayName("If request for getting products proceed then status should be redirected")
    void getProducts_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(creditProductClient.getProducts()).thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/credit-products")).andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting products proceed then response body should be redirected")
    void getActiveProducts_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(creditProductClient.getProducts()).thenReturn(ResponseEntity.ok(responseProducts));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/credit-products"))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(responseProducts), result.getResponse().getContentAsString());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
    }
}