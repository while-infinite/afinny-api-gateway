package by.afinny.apigateway.controller.userservice;

import by.afinny.apigateway.dto.userservice.ChangingEmailDto;
import by.afinny.apigateway.dto.userservice.MobilePhoneDto;
import by.afinny.apigateway.dto.userservice.NotificationChangerDto;
import by.afinny.apigateway.dto.userservice.NotificationDto;
import by.afinny.apigateway.dto.userservice.PasswordDto;
import by.afinny.apigateway.dto.userservice.SecurityDto;
import by.afinny.apigateway.openfeign.userservice.UserClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.assertj.core.api.SoftAssertions;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserClient userClient;

    private final String CLIENT_ID = "d2a25f45-9081-403f-ad3c-c17e8177d878";

    @Captor
    private ArgumentCaptor<UUID> parameterCaptor;
    private PasswordDto passwordDto;
    private SecurityDto securityDto;
    private NotificationDto notificationDto;
    private NotificationChangerDto notificationChangerDto;
    private ChangingEmailDto changingEmailDto;
    private MobilePhoneDto mobilePhoneDto;

    @BeforeAll
    void createDto() {
        passwordDto = PasswordDto.builder()
                .password("qwerty123")
                .newPassword("pows!kfl__013nm").build();
        securityDto = SecurityDto.builder()
                .securityQuestion("what is your favorite cartoon")
                .securityAnswer("shrek").build();
        notificationDto = NotificationDto.builder()
                .smsNotification(false)
                .pushNotification(false)
                .emailSubscription(true)
                .email("dummy_email1337@gmail.com").build();
        notificationChangerDto = NotificationChangerDto.builder()
                .notificationStatus(true).build();
        changingEmailDto = ChangingEmailDto.builder()
                .newEmail("chendler_seen01@mail.ru").build();
        mobilePhoneDto = MobilePhoneDto.builder()
                .mobilePhone("8666412211").build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for changing password proceed then status should be redirected")
    void changePassword_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.changePassword(any(PasswordDto.class), parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/user/settings/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for changing security data proceed then status should be redirected")
    void changeSecurityData_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.changeSecurityData(any(SecurityDto.class), parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/user/settings/controls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(securityDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for getting settings proceed then status should be redirected")
    void getNotificationSettings_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.getNotifications(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).body(notificationDto));
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/user/settings/notifications/all")).andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @Test
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for getting settings changing security data proceed then body should be redirected")
    void getNotificationSettings_shouldRedirectBody() throws Exception {
        //ARRANGE
        when(userClient.getNotifications(parameterCaptor.capture()))
                .thenReturn(ResponseEntity.ok(notificationDto));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(get("/api/v1/user/settings/notifications/all"))
                .andExpect(status().isOk())
                .andReturn();
        verifyClientIdParameter(parameterCaptor.getValue());
        verifyBody(asJsonString(notificationDto), result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for changing sms notifications proceed then status should be redirected")
    void changeSmsNotification_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.changeSmsNotification(any(NotificationChangerDto.class), parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/user/settings/notifications/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(notificationChangerDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for changing push notifications proceed then status should be redirected")
    void changePushNotification_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.changePushNotification(any(NotificationChangerDto.class), parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/user/settings/notifications/push")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(notificationChangerDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for changing email notifications proceed then status should be redirected")
    void changeEmailNotification_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.setEmailSubscription(any(NotificationChangerDto.class), parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/user/settings/notifications/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(notificationChangerDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for changing email proceed then status should be redirected")
    void changeEmail_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.changeEmail(any(ChangingEmailDto.class), parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/user/settings/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(changingEmailDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }
    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 500})
    @WithMockUser(username = CLIENT_ID)
    @DisplayName("If request for changing mobile phone proceed then status should be redirected")
    void changeMobilePhone_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(userClient.changeMobilePhone(any(MobilePhoneDto.class), parameterCaptor.capture()))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(patch("/api/v1/user/settings/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mobilePhoneDto)))
                .andExpect(status().is(httpStatus));
        verifyClientIdParameter(parameterCaptor.getValue());
    }

    private void verifyClientIdParameter(UUID clientIdParameter) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(clientIdParameter)
                .withFailMessage("client id parameter should not be null")
                .isNotNull();
        softAssertions.assertThat(clientIdParameter.toString())
                .withFailMessage("client id should be " + CLIENT_ID + " instead of " + clientIdParameter)
                .isEqualTo(CLIENT_ID);
        softAssertions.assertAll();
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }
}
