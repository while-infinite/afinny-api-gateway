package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.JwtDto;
import by.afinny.apigateway.dto.userservice.LoginByPinDto;
import by.afinny.apigateway.dto.userservice.LoginDto;
import by.afinny.apigateway.dto.userservice.SetupPasswordDto;
import by.afinny.apigateway.dto.userservice.constant.AuthenticationType;
import by.afinny.apigateway.exception.handler.ExceptionHandlerController;
import by.afinny.apigateway.openfeign.userservice.AuthenticationClient;
import by.afinny.apigateway.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class AuthenticationControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private AuthenticationClient authenticationClient;

    private final String TEST_MOBILE_PHONE = "+79184872297";

    @Captor
    private ArgumentCaptor<String> argumentCaptor;
    private JwtDto tokensPair;
    private LoginDto requestLoginDto;
    private LoginByPinDto requestLoginBuIdDto;
    private SetupPasswordDto requestSetupPasswordDto;

    @BeforeAll
    void createDto() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new AuthenticationController(authenticationService, authenticationClient))
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        tokensPair = JwtDto.builder()
                .accessToken("eyJVCJ9.eypvaG4iLCJpYXQiOjE1M.TYyMzkwMjJ9NdbGE-i7pt6ClAbo")
                .refreshToken("eycHff.eyJzdWIODjkopewikwIn0.YwAMgZIPAxM6dr-Rb6cBUODPS3E").build();
        requestLoginDto = LoginDto.builder()
                .login(TEST_MOBILE_PHONE)
                .password("qwerty123")
                .type(AuthenticationType.PHONE_NUMBER).build();
        requestSetupPasswordDto = SetupPasswordDto.builder().newPassword("jx7-DRW4N7u;4g):").build();
        requestLoginBuIdDto = LoginByPinDto.builder()
                .fingerprint("fingerprint")
                .token("eycHff.eyJzdWIODjkopewikwIn0.YwAMgZIPAxM6dr-Rb6cBUODPS3E").build();
    }

    @Test
    @DisplayName("If credentials are correct then return tokens")
    void authenticateUser_shouldReturnTokens() throws Exception {
        //ARRANGE
        when(authenticationService.login(any(LoginDto.class))).thenReturn(tokensPair);
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestLoginDto)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(tokensPair), result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("If credentials are wrong then return status UNAUTHORIZED")
    void authenticateUser_ifWrongCredentials_thenUnauthorized() throws Exception {
        //ARRANGE
        when(authenticationService.login(any(LoginDto.class))).thenThrow(createTestUnauthorizedException("/login"));
        //ACT & VERIFY
        mockMvc.perform(post(AuthenticationController.URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestLoginDto)))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @DisplayName("If password has been changed then redirect status")
    void setNewPassword_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(authenticationClient.setNewPassword(argumentCaptor.capture(), eq(requestSetupPasswordDto.getNewPassword())))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch(AuthenticationController.URL_LOGIN + AuthenticationController.URL_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestSetupPasswordDto))
                        .param("mobilePhone", TEST_MOBILE_PHONE))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @DisplayName("If token successfully updated return tokens")
    void refreshToken_shouldReturnTokens() throws Exception {
        //ARRANGE
        when(authenticationService.generateNewToken(any(HttpServletRequest.class)))
                .thenReturn(tokensPair);
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get(AuthenticationController.URL_LOGIN + AuthenticationController.URL_TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(tokensPair), result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("If token wasn't successfully updated then return status INTERNAL SERVER ERROR")
    void refreshToken_ifRefreshFailed_thenReturnStatus500() throws Exception {
        //ARRANGE
        when(authenticationService.generateNewToken(any(HttpServletRequest.class)))
                .thenThrow(new RuntimeException());
        //ACT & VERIFY
        mockMvc.perform(get(AuthenticationController.URL_LOGIN + AuthenticationController.URL_TOKEN))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("If credentials are correct then return tokens")
    void authenticateUserByPin_shouldReturnTokens() throws Exception {
        //ARRANGE
        when(authenticationService.loginByPin(any(LoginByPinDto.class))).thenReturn(tokensPair);
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(post(AuthenticationController.URL_LOGIN + AuthenticationController.URL_PIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestLoginBuIdDto)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(tokensPair), result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("If credentials are wrong then return status UNAUTHORIZED")
    void authenticateUserById_ifWrongCredentials_thenUnauthorized() throws Exception {
        //ARRANGE
        when(authenticationService.loginByPin(any(LoginByPinDto.class))).thenThrow(createTestUnauthorizedException("/login/pin"));
        //ACT & VERIFY
        mockMvc.perform(post(AuthenticationController.URL_LOGIN + AuthenticationController.URL_PIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestLoginBuIdDto)))
                .andExpect(status().isUnauthorized());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }

    private FeignException.Unauthorized createTestUnauthorizedException(String url) {
        Request request = Request.create(Request.HttpMethod.POST, url, new HashMap<>(), null, new RequestTemplate());
        return new FeignException.Unauthorized("Any message", request, null, null);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}
