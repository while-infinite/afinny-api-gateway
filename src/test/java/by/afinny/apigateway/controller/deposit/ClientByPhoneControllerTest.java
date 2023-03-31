package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.ClientDto;
import by.afinny.apigateway.dto.deposit.constant.CurrencyCode;
import by.afinny.apigateway.exception.handler.ExceptionHandlerController;
import by.afinny.apigateway.openfeign.deposit.FindByPhoneClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientByPhoneController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ClientByPhoneControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FindByPhoneClient findByPhoneClient;

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    private final String FIRST_NAME = "Ivan";
    private final String LAST_NAME = "Ivanov";
    private final String MIDDLE_NAME = "Ivanovich";
    private final String ACCOUNT_NUMBER = "accountNumber";
    private final String MOBILE_PHONE = "+79999999999";
    private final CurrencyCode CURRENCY_CODE = CurrencyCode.RUB;

    @Captor
    private ArgumentCaptor<String> parameterCaptor;
    @Captor
    private ArgumentCaptor<CurrencyCode> currencyCaptor;
    private ClientDto clientDto;

    @BeforeEach
    void setUp() {
        clientDto = ClientDto.builder()
                .clientId(UUID.fromString(TEST_CLIENT_ID))
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME)
                .lastName(LAST_NAME)
                .accountNumber(ACCOUNT_NUMBER)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if get client by phone And currency code then status should be redirected")
    void getClientByPhone_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        Mockito.when(findByPhoneClient.getClientByPhone(UUID.fromString(TEST_CLIENT_ID), MOBILE_PHONE, CURRENCY_CODE))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(MockMvcRequestBuilders.get(ClientByPhoneController.URL_CLIENT_BY_PHONE)
                        .param("mobilePhone", MOBILE_PHONE)
                        .param("currency_code", CURRENCY_CODE.name()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("if get client by phone And currency code then response body should be redirected")
    void getClientByPhone_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        Mockito.when(findByPhoneClient.getClientByPhone(Mockito.eq(UUID.fromString(TEST_CLIENT_ID)),
                        parameterCaptor.capture(),
                        currencyCaptor.capture()))
                .thenReturn(ResponseEntity.ok(clientDto));
        //ACT
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(ClientByPhoneController.URL_CLIENT_BY_PHONE)
                        .param("mobilePhone", MOBILE_PHONE)
                        .param("currency_code", CURRENCY_CODE.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //VERIFY
        verifyFindByPhoneClientParameters(parameterCaptor.getValue(), currencyCaptor.getValue());
        verifyBody(asJsonString(clientDto), result.getResponse().getContentAsString());
    }

    private void verifyFindByPhoneClientParameters(String mobilePhone, CurrencyCode currencyCode) {
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(mobilePhone)
                    .withFailMessage("mobilePhone parameter must not be null")
                    .isNotNull();
            softAssertions.assertThat(mobilePhone)
                    .withFailMessage("mobilePhone must be " + MOBILE_PHONE + " instead of " + mobilePhone)
                    .isEqualTo(MOBILE_PHONE);
            softAssertions.assertThat(currencyCode)
                    .withFailMessage("currencyCode parameter must not be null")
                    .isNotNull();
            softAssertions.assertThat(currencyCode)
                    .withFailMessage("currencyCode must be " + CURRENCY_CODE + " instead of " + currencyCode)
                    .isEqualTo(CURRENCY_CODE);
        });
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
    }
}
