package by.afinny.apigateway.controller.investments;

import by.afinny.apigateway.dto.investments.*;
import by.afinny.apigateway.openfeign.investments.InvestmentClient;
import by.afinny.apigateway.service.investments.InvestmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static by.afinny.apigateway.dto.investments.constant.AssetType.STOCK;
import static by.afinny.apigateway.dto.investments.constant.DealType.PURCHASE_OF_AN_ASSET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvestmentController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class InvestmentControllerTest {
    private final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0001-0000-000000000001");
    private final String TEST_CLIENT_ID = "cc6588da-ffaf-4c00-a3bd-2e0c6d83655d";
    private final UUID BROKERAGE_ACCOUNT_ID = UUID.fromString("00000000-0000-0002-0000-000000000002");
    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvestmentService investmentService;

    @MockBean
    private static InvestmentClient investmentClient;

    private AvailableStocksDto availableStocksDTO;

    private final AvailableStocksDto.AllAvailableStocks allAvailableStocks = new AvailableStocksDto.AllAvailableStocks();


    private final String CLIENT_ID = "9ab1859a-9e29-11ed-a8fc-0242ac120002";

    private static final String CURRENCIES_AND_METALS_URL = "/api/v1/investment/currency";
    private List<BrokerageAccountDto> responseBrokerageAccounts;
    private AvailableCurrenciesDto.AllAvailableCurrencies allAvailableCurrenciesDtoList;
    private AvailableCurrenciesDto.AllAvailableCurrencies expectedAllAvailableCurrenciesDtoList;

    private static ResponseDealDto responseDealDto;
    private static List<ResponseDealDto> responseDealDtoList;


    private static RequestNewPurchaseDto requestNewPurchaseDto;
    private ResponseNewPurchaseDto responseNewPurchaseDto;

    @BeforeAll
    void setUp() {
        availableStocksDTO = AvailableStocksDto.builder()
                .secId("Str")
                .boardId("BID")
                .bId(1)
                .offer(BigDecimal.valueOf(22))
                .last(BigDecimal.valueOf(30))
                .change(8)
                .lastTopRevPrice(BigDecimal.valueOf(20))
                .build();
        allAvailableStocks.setAvailableStocksDtoList(List.of(availableStocksDTO));

        BrokerageAccountDto responseBrokerageAccountDto = BrokerageAccountDto.builder()
                .brokerageAccountId(UUID.randomUUID())
                .nameAccount("tolik")
                .brokerageAccountQuantity(100)
                .build();

        responseBrokerageAccounts = List.of(responseBrokerageAccountDto);

        responseDealDtoList = new ArrayList<>();
        responseDealDto = ResponseDealDto.builder()
                .id(UUID.randomUUID())
                .assetId(UUID.randomUUID())
                .dealType("test")
                .purchasePrice(BigDecimal.valueOf(1000))
                .sellingPrice(BigDecimal.valueOf(1000))
                .sum(BigDecimal.valueOf(1000))
                .dateDeal(LocalDate.now())
                .commission(BigDecimal.valueOf(1000))
                .build();
        responseDealDtoList.add(responseDealDto);

        requestNewPurchaseDto = RequestNewPurchaseDto.builder()
                .idBrokerageAccount(DEFAULT_ID)
                .idAsset(DEFAULT_ID)
                .amount(5)
                .BID(new BigDecimal(50))
                .assetType(STOCK)
                .dealType(PURCHASE_OF_AN_ASSET)
                .dateDeal(LocalDate.now())
                .build();

        responseNewPurchaseDto = ResponseNewPurchaseDto.builder()
                .asset_id(DEFAULT_ID)
                .amount(5)
                .purchase_price(new BigDecimal(50))
                .asset_type(STOCK)

                .build();

    }

    @BeforeEach
    void setUpBeforEach() {
        List<AvailableCurrenciesDto> currencies = List.of(
                AvailableCurrenciesDto.builder()
                        .secid("EUR000TODTOM")
                        .boardid("AUCB")
                        .last(0.0)
                        .change(0.0)
                        .lasttoprevprice(0.0)
                        .build(),
                AvailableCurrenciesDto.builder()
                        .secid("EURRUB_SPT")
                        .boardid("AUCB")
                        .last(0.0)
                        .change(0.0)
                        .lasttoprevprice(0.0)
                        .build(),
                AvailableCurrenciesDto.builder()
                        .secid("EURRUB_TOD1D")
                        .boardid("AUCB")
                        .last(0.0)
                        .change(0.0)
                        .lasttoprevprice(0.0)
                        .build()
        );

        allAvailableCurrenciesDtoList = new AvailableCurrenciesDto.AllAvailableCurrencies();
        allAvailableCurrenciesDtoList.setCurrencies(currencies);

        expectedAllAvailableCurrenciesDtoList = new AvailableCurrenciesDto.AllAvailableCurrencies();
        expectedAllAvailableCurrenciesDtoList.setCurrencies(currencies);
    }

    @Test
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("if the list of deal was successfully received then return status OK")
    void getDetailsDeals_shouldReturnResponseDeal() throws Exception {
        //ARRANGE
        when(investmentClient.getDetailsDeals(BROKERAGE_ACCOUNT_ID, UUID.fromString(CLIENT_ID), 0, 4)).thenReturn(ResponseEntity.ok(responseDealDtoList));
        //ACT
        MvcResult mvcResult = mockMvc.perform(get(
                        "/api/v1/investment" +
                                "/history")
                        .param("brokerageAccountId", String.valueOf(BROKERAGE_ACCOUNT_ID))
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(4))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(responseDealDtoList)))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyBody(asJsonString(responseDealDtoList), mvcResult.getResponse().getContentAsString());
    }

    @WithMockUser(username = TEST_CLIENT_ID)
    @ParameterizedTest
    @ValueSource(ints = {500})
    @DisplayName("if the list of deal wasn't successfully received then return Internal Server Error")
    void getDetailsDeals_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(investmentClient.getDetailsDeals(BROKERAGE_ACCOUNT_ID, UUID.fromString(TEST_CLIENT_ID), 0, 4)).thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/investment" +
                        "/history")
                        .param("brokerageAccountId", String.valueOf(BROKERAGE_ACCOUNT_ID))
                        .param("pageNumber", "0"))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("if request for getting stocks from Moscow Exchange success then  must be returned all stocks")
    public void getAllAvailableStocksFromMoscowExchangeApi_success_thenReturnListOfAvailableStocksDTO() throws Exception {
        when(investmentService.getAllAvailableStocks()).thenReturn(allAvailableStocks);

        MvcResult result = mockMvc.perform(get("/api/v1/investment/address")).andExpect(status().isOk()).andReturn();
        verifyBody(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(allAvailableStocks));

    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if request for getting a data from Moscow Exchange proceed then data must be returned to user")
    void getCurrenciesAndPreciousMetals_shouldReturnData() throws Exception {
        //ARRANGE
        when(investmentService.getAllAvailableCurrencies())
                .thenReturn(allAvailableCurrenciesDtoList);
        //ACT
        MvcResult mvcResult = mockMvc.perform(get(CURRENCIES_AND_METALS_URL))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyBody(
                new ObjectMapper().writeValueAsString(expectedAllAvailableCurrenciesDtoList),
                mvcResult.getResponse().getContentAsString()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting current brokerageAccounts proceed then status must be redirected")
    void getClientCurrentBrokerageAccounts_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(investmentClient.getClientBrokerageAccounts(clientIdCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());

        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/investment")).andExpect(status().is(httpStatus));
        verifyClientIdParameter(clientIdCaptor.getValue());
    }


    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting current brokerageAccount proceed then response body must be redirected")
    void getClientCurrentBrokerageAccounts_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(investmentClient.getClientBrokerageAccounts(clientIdCaptor.capture()))
                .thenReturn(ResponseEntity.ok(responseBrokerageAccounts));

        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/investment"))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(clientIdCaptor.getValue());
        verifyBody(asJsonString(responseBrokerageAccounts), result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for getting current brokerageAccount proceed then response body must be redirected")
    void getClientCurrentBrokerageAccount_shouldRedirectResponseBodyAndStatus() throws Exception {
        //ARRANGE
        when(investmentClient.getClientBrokerageAccount(clientIdCaptor.capture()))
                .thenReturn(ResponseEntity.ok(new BrokerageAccountInfoDto()));

        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/investment/brokerageAccountInfo/{brokerageAccountId}", CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(status().isOk());
        verifyBody(asJsonString(new BrokerageAccountInfoDto()), result.getResponse().getContentAsString());
    }

    private void verifyClientIdParameter(UUID clientIdParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(clientIdParameter)
                .withFailMessage("Client id parameter should not be null")
                .isNotNull();
        softAssertions.assertThat(clientIdParameter.toString())
                .withFailMessage("Client id should be " + TEST_CLIENT_ID + " instead of " + clientIdParameter)
                .isEqualTo(TEST_CLIENT_ID);
        softAssertions.assertAll();
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .findAndRegisterModules()
                .writeValueAsString(obj);
    }

}

