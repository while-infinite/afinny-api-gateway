package by.afinny.apigateway.controller.investments;

import by.afinny.apigateway.dto.investments.RequestNewAccountAgreeDto;
import by.afinny.apigateway.dto.investments.ResponseNewAccountAgreeDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.mapper.investments.NewAccountAgreeDtoMapper;
import by.afinny.apigateway.openfeign.investments.OrderClient;
import by.afinny.apigateway.openfeign.userservice.InformationClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class OrderControllerTest {

    private final String STRING_CLIENT_ID = "e6376def-e541-4f03-aa0e-b7fc6fe4e1aa";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InformationClient informationClient;
    @MockBean
    private OrderClient orderClient;
    @MockBean
    private NewAccountAgreeDtoMapper newAccountAgreeDtoMapper;
    @Mock
    private Authentication authentication;
    private ResponseNewAccountAgreeDto responseNewAccountAgreeDto;
    private RequestNewAccountAgreeDto requestNewAccountAgreeDto;
    private ResponseClientDataDto responseClientDataDto;

    @BeforeEach
    void setUp() {
        responseClientDataDto = ResponseClientDataDto.builder()
                .firstName("Tomas")
                .middleName("Neo")
                .lastName("Anderson")
                .mobilePhone("77777777777")
                .clientId(STRING_CLIENT_ID)
                .email("white_rabbit@matrix.com")
                .passportNumber("777777")
                .build();

        responseNewAccountAgreeDto = ResponseNewAccountAgreeDto.builder()
                .agreeDate(LocalDate.now())
                .firstName("Tomas")
                .middleName("Neo")
                .lastName("Anderson")
                .phoneNumber("77777777777")
                .clientId(STRING_CLIENT_ID)
                .email("white_rabbit@matrix.com")
                .passportNumber("777777")
                .build();

        requestNewAccountAgreeDto = RequestNewAccountAgreeDto.builder()
                .firstName("Tomas")
                .middleName("Neo")
                .lastName("Anderson")
                .phoneNumber("77777777777")
                .clientId(STRING_CLIENT_ID)
                .email("white_rabbit@matrix.com")
                .passportNumber("777777")
                .build();

        when(authentication.getName())
                .thenReturn(STRING_CLIENT_ID);
    }

    @Test
    @WithMockUser(username = STRING_CLIENT_ID)
    @DisplayName("when invoked without body should return ResponseNewInvestmentsAccountAgreeDto and status ok")
    void testCreateNewAccount_WhenInvokedWithoutBody_ShouldReturnResponseNewInvestmentsAccountAgreeDto() throws Exception {
        when(informationClient.getClientData(any(UUID.class)))
                .thenReturn(ResponseEntity.ok(responseClientDataDto));
        when(newAccountAgreeDtoMapper.toRequestNewAccountAgree(responseClientDataDto))
                .thenReturn(requestNewAccountAgreeDto);
        when(orderClient.createAccountAgree(requestNewAccountAgreeDto))
                .thenReturn(ResponseEntity.ok(responseNewAccountAgreeDto));

        mockMvc.perform(post("/api/v1/investment-order/new-account"))
                .andExpect(status().isOk());
    }


}