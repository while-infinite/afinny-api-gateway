package by.afinny.apigateway.controller.infoservice;


import by.afinny.apigateway.dto.infoservice.CurrencyExchangeRateDto;
import by.afinny.apigateway.dto.infoservice.ResponseExchangeRateDto;
import by.afinny.apigateway.dto.infoservice.constant.CurrencyCode;
import by.afinny.apigateway.dto.investments.AvailableCurrenciesDto;
import by.afinny.apigateway.mapper.infoservice.ResponseExchangeRateDtoMapper;
import by.afinny.apigateway.openfeign.infoservice.ExchangeRateClient;
import by.afinny.apigateway.service.investments.InvestmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ExchangeRateController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ExchangeRateControllerTest {
        private final String SELLING_RATE = "75.10";
        private final String BUYING_RATE = "65.10";

        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private InvestmentService investmentService;
        @MockBean
        private ExchangeRateClient exchangeRateClient;
        private List<ResponseExchangeRateDto> responseExchangeRates;
        private CurrencyExchangeRateDto exchangeRateDto;
        @MockBean
        private ResponseExchangeRateDtoMapper responseExchangeRateDtoMapper;
        private AvailableCurrenciesDto.AllAvailableCurrencies allAvailableCurrenciesDtoList;
        private ResponseExchangeRateDto.ResponseExchangeRateList responseExchangeRateList;

        @BeforeAll
        void createProducts() {

            List<AvailableCurrenciesDto> currencies = List.of(
                    AvailableCurrenciesDto.builder()
                            .secid("EUR000TODTOM")
                            .boardid("AUCB")
                            .last(100.0)
                            .change(0.0)
                            .lasttoprevprice(0.0)
                            .build(),
                    AvailableCurrenciesDto.builder()
                            .secid("EURRUB_SPT")
                            .boardid("AUCB")
                            .last(100.0)
                            .change(0.0)
                            .lasttoprevprice(0.0)
                            .build()
            );

            allAvailableCurrenciesDtoList = new AvailableCurrenciesDto.AllAvailableCurrencies();
            allAvailableCurrenciesDtoList.setCurrencies(currencies);

            List<ResponseExchangeRateDto> responseExchangeRateDtoList = List.of(
                    ResponseExchangeRateDto.builder()
                            .secid("EUR000TODTOM")
                            .boardid("AUCB")
                            .buyingRate(105.0)
                            .sellingRate(95.0)
                            .build(),
                    ResponseExchangeRateDto.builder()
                            .secid("EURRUB_SPT")
                            .boardid("AUCB")
                            .buyingRate(105.0)
                            .sellingRate(95.0)
                            .build()
            );

            responseExchangeRateList = new ResponseExchangeRateDto.ResponseExchangeRateList();
            responseExchangeRateList.setExchangeRates(responseExchangeRateDtoList);

            exchangeRateDto = CurrencyExchangeRateDto.builder()
                    .currencyRate(calculateCurrencyExchangeRate())
                    .build();

        }

        @Test
        @WithMockUser
        @DisplayName("If investmentService response wasn't successfully then return status INTERNAL SERVER ERROR")
        void getActualExchangeRates_ifInvestmentServiceNotSuccess_then_INTERNAL_SERVER_ERROR() throws Exception {
            //ARRANGE
            when(investmentService.getAllAvailableCurrencies()).thenThrow(new RuntimeException());

            //ACT & VERIFY
            mockMvc.perform(get("/api/v1/exchange-rates"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @WithMockUser
        @DisplayName("If responseExchangeRateDtoMapper convertation wasn't successfully then return status INTERNAL SERVER ERROR")
        void getActualExchangeRates_ifResponseExchangeRateDtoMapperNotSuccess_then_INTERNAL_SERVER_ERROR() throws Exception {
            //ARRANGE
            when(investmentService.getAllAvailableCurrencies()).thenReturn(allAvailableCurrenciesDtoList);
            when(responseExchangeRateDtoMapper.toResponseExchangeRateDtoList(allAvailableCurrenciesDtoList))
                    .thenThrow(new RuntimeException());

            //ACT & VERIFY
            mockMvc.perform(get("/api/v1/exchange-rates"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @WithMockUser
        @DisplayName("If request for getting exchange rates proceed then response body should be redirected")
        void getActualExchangeRates_shouldRedirectResponseBody() throws Exception {
            //ARRANGE
            when(investmentService.getAllAvailableCurrencies()).thenReturn(allAvailableCurrenciesDtoList);
            when(responseExchangeRateDtoMapper.toResponseExchangeRateDtoList(allAvailableCurrenciesDtoList))
                    .thenReturn(responseExchangeRateList);
            //ACT & VERIFY
            MvcResult mvcResult = mockMvc.perform(get("/api/v1/exchange-rates"))
                    .andExpect(status().isOk())
                    .andReturn();
            verifyBody(asJsonString(responseExchangeRateList), mvcResult.getResponse().getContentAsString());
        }

        @ParameterizedTest
        @ValueSource(ints = {200, 401, 500})
        @WithMockUser
        @DisplayName("if request for getting currency exchange rate proceed then status should be redirected")
        void getCurrencyExchangeRates_shouldRedirectStatus(int httpStatus) throws Exception {
            //ARRANGE
            when(exchangeRateClient.getCurrencyExchangeRate(SELLING_RATE, BUYING_RATE))
                    .thenReturn(ResponseEntity.status(httpStatus).build());
            //ACT & VERIFY
            mockMvc.perform(get("/api/v1/rates")
                            .param("baseCurrency", SELLING_RATE)
                            .param("currency", BUYING_RATE))
                    .andExpect(status().is(httpStatus));
        }

        @Test
        @WithMockUser
        @DisplayName("if request for getting currency exchange rate proceed then response body should be redirected")
        void getCurrencyExchangeRates_shouldRedirectResponseBody() throws Exception {
            //ARRANGE
            when(exchangeRateClient.getCurrencyExchangeRate(SELLING_RATE, BUYING_RATE))
                    .thenReturn(ResponseEntity.ok(exchangeRateDto));
            //ACT & VERIFY
            MvcResult result = mockMvc.perform(get("/api/v1/rates")
                            .param("baseCurrency", SELLING_RATE)
                            .param("currency", BUYING_RATE))
                    .andExpect(status().isOk())
                    .andReturn();
            verifyBody(asJsonString(exchangeRateDto), result.getResponse().getContentAsString());
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

        private String calculateCurrencyExchangeRate() {
            double sellingRate = Double.parseDouble(SELLING_RATE);
            double buyingRate = Double.parseDouble(BUYING_RATE);
            double result = sellingRate / buyingRate;
            return String.format("%.2f", result).replace(",", ".");
        }

}