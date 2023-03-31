package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.controller.deposit.CardController;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.exception.handler.ExceptionHandlerController;
import by.afinny.apigateway.openfeign.userservice.InformationClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InformationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class InformationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InformationClient informationClient;

    private final String CLIENT_ID = "d3a3b50d-a16f-4eb3-a03a-a18eb308a3b1";

    @Captor
    private ArgumentCaptor<UUID> parameterCaptor;
    private ResponseClientDataDto responseClientDataDto;

    @BeforeAll
    void createDto() {
        responseClientDataDto = ResponseClientDataDto.builder()
                .firstName("Anton")
                .middleName("Viktorovich")
                .lastName("Gorohov")
                .mobilePhone("9237347017")
                .email("ngnipalm@vusra.com")
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("if request for getting personal user data then status should be redirected")
    void getClientData_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(informationClient.getClientData(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get(InformationController.INFORMATION_URL)
                        .param(InformationController.CLIENT_ID_PARAMETER, CLIENT_ID))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @Test
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for getting personal user data then response body should be redirected")
    void getDeposit_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(informationClient.getClientData(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.ok(responseClientDataDto));
        //ACT
        MvcResult result = mockMvc.perform(get(InformationController.INFORMATION_URL)
                        .param(InformationController.CLIENT_ID_PARAMETER, CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyClientIdParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(responseClientDataDto), result.getResponse().getContentAsString());
    }

    private void verifyClientIdParameter(UUID clientIdParameter) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(clientIdParameter)
                    .withFailMessage("Client id parameter must not be null")
                    .isNotNull();
            softAssertions.assertThat(clientIdParameter.toString())
                    .withFailMessage("Client id must be " + CLIENT_ID + " instead of " + clientIdParameter)
                    .isEqualTo(CLIENT_ID);
        });
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
    }
}