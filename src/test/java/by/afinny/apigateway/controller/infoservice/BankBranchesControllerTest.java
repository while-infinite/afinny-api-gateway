package by.afinny.apigateway.controller.infoservice;

import by.afinny.apigateway.dto.infoservice.ResponseBankBranchDto;
import by.afinny.apigateway.dto.infoservice.ResponseBranchCoordinatesDto;
import by.afinny.apigateway.dto.infoservice.constant.BankBranchType;
import by.afinny.apigateway.openfeign.infoservice.BankBranchClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankBranchesController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class BankBranchesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankBranchClient bankBranchClient;
    private List<ResponseBankBranchDto> responseBankBranches;
    private List<ResponseBranchCoordinatesDto> responseBranchCoordinatesDtoList;

    @BeforeEach
    void createProducts() {
        ResponseBankBranchDto bankBranch = ResponseBankBranchDto.builder()
                .bankBranchType(BankBranchType.ATM)
                .branchNumber("123")
                .branchCoordinates("55.786386, 37.682488")
                .city("Moscow")
                .branchAddress("Gestalo, 6")
                .closed(false)
                .openingTime(Time.valueOf(LocalTime.of(8, 30)))
                .closingTime(Time.valueOf(LocalTime.of(20, 0)))
                .workAtWeekends(true)
                .cashWithdraw(true)
                .moneyTransfer(true)
                .acceptPayment(true)
                .currencyExchange(true)
                .exoticCurrency(false)
                .ramp(false)
                .replenishCard(true)
                .replenishAccount(true)
                .consultation(true)
                .insurance(false)
                .replenishWithoutCard(false)
                .build();

        responseBankBranches = List.of(bankBranch);

        responseBranchCoordinatesDtoList = Collections.singletonList(
                ResponseBranchCoordinatesDto.builder()
                        .id(UUID.randomUUID())
                        .branchCoordinates("55.786386, 37.682488")
                        .build());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser
    @DisplayName("If request for getting bank branches proceed then status should be redirected")
    void getActualExchangeRates_shouldRedirectStatus(int status) throws Exception {
        //ARRANGE
        when(bankBranchClient.getAllBankBranches())
                .thenReturn(ResponseEntity.status(status).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/bank-branch"))
                .andExpect(status().is(status));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting bank branches proceed then response body should be redirected")
    void getActualExchangeRates_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(bankBranchClient.getAllBankBranches())
                .thenReturn(ResponseEntity.ok(responseBankBranches));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bank-branch"))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(responseBankBranches), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser
    @DisplayName("If request for getting filtered bank branches proceed then status should be redirected")
    void getFilteredBankBranches_shouldRedirectStatus(int status) throws Exception {
        //ARRANGE
        when(bankBranchClient.getFilteredBankBranches(BankBranchType.ATM, false, false,
                false, false, false, false, false,
                false, false, false, false, false, false))
                .thenReturn(ResponseEntity.status(status).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/bank-branch/filters")
                .param("bankBranchType", BankBranchType.ATM.name())
                .param("closed", "false")
                .param("workAtWeekends", "false")
                .param("cashWithdraw", "false")
                .param("moneyTransfer", "false")
                .param("acceptPayment", "false")
                .param("currencyExchange", "false")
                .param("exoticCurrency", "false")
                .param("ramp", "false")
                .param("replenishCard", "false")
                .param("replenishAccount", "false")
                .param("consultation", "false")
                .param("insurance", "false")
                .param("replenishWithoutCard", "false"))
                .andExpect(status().is(status));
    }

    @Test
    @WithMockUser
    @DisplayName("If request fo getting filtered bank branches proceed then response body should be redirected")
    void getFilteredBankBranches_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(bankBranchClient.getFilteredBankBranches(BankBranchType.ATM, false, false,
                false, false, false, false, false,
                false, false, false, false, false, false))
                .thenReturn(ResponseEntity.ok(responseBranchCoordinatesDtoList));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bank-branch/filters")
                .param("bankBranchType", BankBranchType.ATM.name())
                .param("closed", "false")
                .param("workAtWeekends", "false")
                .param("cashWithdraw", "false")
                .param("moneyTransfer", "false")
                .param("acceptPayment", "false")
                .param("currencyExchange", "false")
                .param("exoticCurrency", "false")
                .param("ramp", "false")
                .param("replenishCard", "false")
                .param("replenishAccount", "false")
                .param("consultation", "false")
                .param("insurance", "false")
                .param("replenishWithoutCard", "false"))
                .andReturn();
        verifyBody(asJsonString(responseBranchCoordinatesDtoList),mvcResult.getResponse().getContentAsString());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

}