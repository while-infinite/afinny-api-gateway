package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.ClientDto;
import by.afinny.apigateway.dto.userservice.PassportDto;
import by.afinny.apigateway.dto.userservice.RegisteredUserDto;
import by.afinny.apigateway.dto.userservice.RegisteringUserDto;
import by.afinny.apigateway.dto.userservice.RequestClientDto;
import by.afinny.apigateway.dto.userservice.RequestRegisteringUserDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDto;
import by.afinny.apigateway.dto.userservice.UserDto;
import by.afinny.apigateway.dto.userservice.constant.ClientStatus;
import by.afinny.apigateway.exception.dto.AccountExistErrorDto;
import by.afinny.apigateway.mapper.ClientDtoMapperImpl;
import by.afinny.apigateway.mapper.RegisteringUserDtoMapper;
import by.afinny.apigateway.openfeign.userservice.RegistrationClient;
import by.afinny.apigateway.service.insurance.impl.ClientServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class RegistrationControllerTest {

    private final String TEST_CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegisteringUserDtoMapper registeringUserDtoMapper;
    @MockBean
    private RegistrationClient registrationClient;
    @SpyBean
    private ClientDtoMapperImpl clientDtoMapper;
    @MockBean
    private ClientServiceImpl clientService;
    private final String TEST_MOBILE_PHONE = "79184872297";
    @Captor
    private ArgumentCaptor<String> parameterCaptor;
    private ClientDto clientResponseDto;
    private UserDto userResponseDto;
    private RequestRegisteringUserDto requestRegisteringUserDto;
    private RegisteringUserDto registeringUserDto;
    private RegisteredUserDto registeredUserDto;
    private RequestClientDto requestClientDto;
    private ResponseClientDto responseClientDto;
    private PassportDto passportDto;

    @BeforeAll
    void createNotClientResponse() {

        requestRegisteringUserDto = RequestRegisteringUserDto.builder()
                .clientStatus(ClientStatus.ACTIVE)
                .mobilePhone(TEST_MOBILE_PHONE)
                .password("qwerty123")
                .securityQuestion("favorite meal")
                .securityAnswer("pasta")
                .email("sssdddzx2022@gmail.com")
                .build();
        userResponseDto = UserDto.builder()
                .mobilePhone(TEST_MOBILE_PHONE)
                .clientStatus(ClientStatus.NOT_CLIENT).build();
        registeringUserDto = RegisteringUserDto.builder()
                .id(UUID.randomUUID())
                .clientStatus(ClientStatus.ACTIVE)
                .mobilePhone(TEST_MOBILE_PHONE)
                .password("qwerty123")
                .securityQuestion("favorite meal")
                .securityAnswer("pasta")
                .email("sssdddzx2022@gmail.com").build();
        registeredUserDto = RegisteredUserDto.builder()
                .id(UUID.randomUUID())
                .clientStatus(registeringUserDto.getClientStatus())
                .mobilePhone(registeringUserDto.getMobilePhone())
                .password(registeringUserDto.getPassword())
                .securityQuestion(registeringUserDto.getSecurityQuestion())
                .securityAnswer(registeringUserDto.getSecurityAnswer())
                .email(registeringUserDto.getEmail())
                .smsNotification(true)
                .pushNotification(true).build();
        requestClientDto = RequestClientDto.builder()
                .mobilePhone(TEST_MOBILE_PHONE)
                .passportNumber("33456210")
                .securityQuestion("cat's name")
                .securityAnswer("cucumber")
                .email("p.johnson2013@outlook.com")
                .firstName("Peter")
                .lastName("Johnson")
                .password("lkmvnoo012")
                .countryOfResidence(true).build();
        responseClientDto = ResponseClientDto.builder()
                .id(UUID.randomUUID())
                .smsNotification(true)
                .pushNotification(false)
                .mobilePhone(TEST_MOBILE_PHONE)
                .passportNumber("33456210")
                .securityQuestion("cat's name")
                .securityAnswer("cucumber")
                .email("p.johnson2013@outlook.com")
                .firstName("Peter")
                .lastName("Johnson")
                .password("lkmvnoo012")
                .countryOfResidence(true).build();
        passportDto = PassportDto.builder()
                .passportNumber("123456")
                .build();
    }

    @BeforeEach
    void createClientDto() {
        clientResponseDto = new ClientDto();
        clientResponseDto.setMobilePhone(TEST_MOBILE_PHONE);
    }

    @Test
    @DisplayName("If received not client then cast it to user")
    void verifyMobilPhone_ifNotClient_thenCastToUser() throws Exception {
        //ARRANGE
        clientHasNotClientStatus();
        when(registrationClient.verifyMobilePhone(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.ok(clientResponseDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/registration")
                        .param("mobilePhone", TEST_MOBILE_PHONE))
                .andExpect(status().isOk())
                .andReturn();
        verifyMobilePhoneParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(userResponseDto), result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("If received not registered client then return client dto with client id")
    void verifyMobilePhone_ifNotRegistered_thenRedirectBody() throws Exception {
        //ARRANGE
        clientHasNotRegisteredStatus();
        when(registrationClient.verifyMobilePhone(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.ok(clientResponseDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/registration")
                        .param("mobilePhone", TEST_MOBILE_PHONE))
                .andExpect(status().isOk())
                .andReturn();
        verifyMobilePhoneParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(clientResponseDto), result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("If received conflict status then redirect body")
    void verifyMobilePhone_ifClientAndRegistered_thenRedirectException() throws Exception {
        //ARRANGE
        AccountExistErrorDto errorDto = new AccountExistErrorDto(ClientStatus.ACTIVE.toString(),
                "409",
                "Account already exist!");
        when(registrationClient.verifyMobilePhone(parameterCaptor.capture()))
                .thenThrow(createTestConflictException(errorDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/registration")
                        .param("mobilePhone", TEST_MOBILE_PHONE))
                .andExpect(status().isConflict())
                .andReturn();
        verifyMobilePhoneParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(errorDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @WithMockUser(username = TEST_CLIENT_ID)
    @ValueSource(ints = {200, 400, 500})
    @DisplayName("If request for registering client proceed then status should be redirected")
    void registerExistingClient_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(clientService.registerExistingClient(UUID.fromString(TEST_CLIENT_ID), requestRegisteringUserDto)).thenReturn(registeringUserDto);
        when(registeringUserDtoMapper.clientToUser(requestRegisteringUserDto,UUID.fromString(TEST_CLIENT_ID)))
                .thenReturn(registeringUserDto);
        when(registrationClient.registerExistingClient(any(RegisteringUserDto.class)))
                .thenReturn(ResponseEntity.ok().build());
        when(registrationClient.registerNonClient(any(RequestClientDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/registration/user-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestRegisteringUserDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for registering client proceed then return ok")
    void registerExistingClient_shouldRegister() throws Exception {
        //ARRANGE
        when(clientService.registerExistingClient(UUID.fromString(TEST_CLIENT_ID), requestRegisteringUserDto)).thenReturn(registeringUserDto);
        when(registeringUserDtoMapper.clientToUser(requestRegisteringUserDto,UUID.fromString(TEST_CLIENT_ID)))
                .thenReturn(registeringUserDto);
        when(registrationClient.registerExistingClient(any(RegisteringUserDto.class)))
                .thenReturn(ResponseEntity.ok().build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/registration/user-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestRegisteringUserDto)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @DisplayName("If request for registering non-client proceed then status should be redirected")
    void registerNonClient_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(registrationClient.registerNonClient(any(RequestClientDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/registration/user-profile/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestClientDto)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @DisplayName("If request for registering non-client proceed then body should be redirected")
    void registerNonClient_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(registrationClient.registerNonClient(any(RequestClientDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        //ACT & VERIFY
        MvcResult result = mockMvc.perform(post("/api/v1/registration/user-profile/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestClientDto)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("If passport number was not fount then return ok")
    void verifyPassportNumber_shouldReturnOk() throws Exception {
        //ARRANGE
        when(registrationClient.verifyPassportNumber(passportDto)).thenReturn(ResponseEntity.ok().build());

        //ACT & VERIFY
        MvcResult result = mockMvc.perform(post("/api/v1/registration/user-profile/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passportDto)))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void clientHasNotClientStatus() {
        clientResponseDto.setClientStatus(ClientStatus.NOT_CLIENT);
    }

    private void clientHasNotRegisteredStatus() {
        clientResponseDto.setId(UUID.randomUUID());
        clientResponseDto.setClientStatus(ClientStatus.NOT_REGISTERED);
    }

    private void verifyMobilePhoneParameter(String mobilePhoneParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(mobilePhoneParameter)
                .withFailMessage("mobile phone parameter should be created")
                .isNotNull();
        softAssertions.assertThat(mobilePhoneParameter)
                .withFailMessage("mobile phone should be " + TEST_MOBILE_PHONE + " instead of " + mobilePhoneParameter)
                .isEqualTo(TEST_MOBILE_PHONE);
        softAssertions.assertAll();
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }

    private FeignException.Conflict createTestConflictException(Object errorDto) throws JsonProcessingException {
        Request request = Request.create(Request.HttpMethod.GET, "/registration", new HashMap<>(), null, new RequestTemplate());
        return new FeignException.Conflict("", request, asJsonString(errorDto).getBytes(StandardCharsets.UTF_8), null);
    }
}