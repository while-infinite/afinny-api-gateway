package by.afinny.apigateway.controller.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CommissionDto;
import by.afinny.apigateway.dto.moneytransfer.constant.CurrencyCode;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferTypeName;
import by.afinny.apigateway.openfeign.moneytransfer.CommissionClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommissionController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CommissionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommissionClient commissionClient;

    private final String FORMAT_DATE = "yyyy-MM-dd";
    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final TransferTypeName typeName = TransferTypeName.BETWEEN_CARDS;
    private final CurrencyCode currencyCode = CurrencyCode.RUB;

    private CommissionDto commissionDto;

    @BeforeAll
    void setUp() {

    commissionDto = CommissionDto.builder()
            .id(1)
            .fixCommission(new BigDecimal(5))
            .percentCommission(new BigDecimal(6))
            .maxCommission(new BigDecimal(10))
            .minCommission(new BigDecimal(2))
            .maxSum(new BigDecimal(50))
            .minSum(new BigDecimal(10))
            .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting commission proceed then status should be redirected")
    void getCommissionData_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(commissionClient.getCommissionData(typeName,currencyCode))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get(CommissionController.COMMISSION_URL)
                        .param("typeName", typeName.toString())
                        .param("currencyCode",currencyCode.toString()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting commission proceed then response body should be redirected")
    void getCommissionData_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(commissionClient.getCommissionData(typeName,currencyCode))
                .thenReturn(ResponseEntity.ok(commissionDto));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get(CommissionController.COMMISSION_URL)
                        .param("typeName", typeName.toString())
                        .param("currencyCode", currencyCode.toString()))
                .andExpect(status().isOk()).andReturn();
        verifyBody(asJsonString(commissionDto),mvcResult.getResponse().getContentAsString());
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
