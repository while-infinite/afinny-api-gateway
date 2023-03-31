package by.afinny.apigateway.mapper;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import by.afinny.apigateway.dto.userservice.RegisteringUserDto;
import by.afinny.apigateway.dto.userservice.RequestRegisteringUserDto;
import by.afinny.apigateway.dto.userservice.constant.ClientStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class RegisteringUserDtoMapperTest {

    @InjectMocks
    private RegisteringUserDtoMapperImpl userDtoMapper;

    private RequestRegisteringUserDto requestRegisteringUserDto;

    private RegisteringUserDto registeringUserDto;
    private final String TEST_MOBILE_PHONE = "79184872297";

    @BeforeAll
    void setUp() {
        requestRegisteringUserDto = RequestRegisteringUserDto.builder()
                .clientStatus(ClientStatus.ACTIVE)
                .mobilePhone(TEST_MOBILE_PHONE)
                .password("qwerty123")
                .securityQuestion("favorite meal")
                .securityAnswer("pasta")
                .email("sssdddzx2022@gmail.com").build();
    }

    @Test
    @DisplayName("Verify registering user dto fields setting")
    void requestRegisteringUserDto_shouldReturnRegisteringUserDto() {
        //ACT
        registeringUserDto = userDtoMapper.clientToUser(requestRegisteringUserDto, UUID.randomUUID());
        //VERIFY
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(registeringUserDto.getEmail()).isEqualTo(requestRegisteringUserDto.getEmail());
            softAssertions.assertThat(registeringUserDto.getPassword()).isEqualTo(requestRegisteringUserDto.getPassword());
            softAssertions.assertThat(registeringUserDto.getMobilePhone()).isEqualTo(requestRegisteringUserDto.getMobilePhone());
            softAssertions.assertThat(registeringUserDto.getSecurityQuestion()).isEqualTo(requestRegisteringUserDto.getSecurityQuestion());
            softAssertions.assertThat(registeringUserDto.getSecurityAnswer()).isEqualTo(requestRegisteringUserDto.getSecurityAnswer());
            softAssertions.assertThat(registeringUserDto.getClientStatus()).isEqualTo(requestRegisteringUserDto.getClientStatus());

        });
    }

}