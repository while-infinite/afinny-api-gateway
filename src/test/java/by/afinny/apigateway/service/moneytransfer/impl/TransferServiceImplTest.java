package by.afinny.apigateway.service.moneytransfer.impl;

import by.afinny.apigateway.dto.moneytransfer.CreatePaymentDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentResponseDto;
import by.afinny.apigateway.dto.moneytransfer.constant.TransferStatus;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.mapper.moneytransfer.TransferMapper;
import by.afinny.apigateway.openfeign.moneytransfer.TransferClient;
import by.afinny.apigateway.openfeign.userservice.InformationClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
class TransferServiceImplTest {

    @InjectMocks
    private TransferServiceImpl transferService;
    @Mock
    private TransferClient transferClient;
    @Mock
    private InformationClient informationClient;
    @Mock
    private TransferMapper transferMapper;
    private CreatePaymentResponseDto createPaymentResponseDto;
    private ResponseClientDataDto responseClientDataDto;

    @BeforeEach
    void setUp() {
        createPaymentResponseDto = CreatePaymentResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .first_name("first_name")
                .last_name("last_name")
                .middle_name("middle_name")
                .created_at(LocalDateTime.now().toString())
                .status(TransferStatus.PERFORMED.name())
                .card_number("0000111100001111")
                .transfer_type_id("1")
                .sum(BigDecimal.valueOf(1000.0).toString())
                .remitter_card_number("0000111100001111")
                .name("name")
                .payee_account_number(UUID.randomUUID().toString())
                .payee_card_number("0000111100001111")
                .mobile_phone("+7999999999")
                .sum_commission(BigDecimal.valueOf(0.0).toString())
                .authorizationCode("1111")
                .currencyExchange(BigDecimal.valueOf(1.0).toString())
                .purpose("purpose")
                .inn("inn")
                .bic("bic")
                .build();

        responseClientDataDto = ResponseClientDataDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .mobilePhone("")
                .build();
    }

    @Test
    @DisplayName("if createPaymentOrTransfer method was successfully then return CreatePaymentOrTransferResponseDto")
    void createPaymentOrTransfer_shouldReturn_CreatePaymentOrTransferResponseDto() {
        //ARRANGE
        when(transferClient.createPaymentOrTransfer(any(UUID.class), any(CreatePaymentDto.class)))
                .thenReturn(ResponseEntity.ok(createPaymentResponseDto));
        when(informationClient.getClientData(any(UUID.class)))
                .thenReturn(ResponseEntity.ok(responseClientDataDto));
        when(transferMapper.toCreatePaymentResponseDto(createPaymentResponseDto, responseClientDataDto))
                .thenReturn(createPaymentResponseDto);

        //ACT
        CreatePaymentResponseDto createPaymentResponseDtoActual = transferService
                .createPaymentOrTransfer(UUID.randomUUID(), new CreatePaymentDto());

        //VERIFY
        assertThat(createPaymentResponseDtoActual).usingRecursiveComparison().isEqualTo(createPaymentResponseDto);
    }

    @Test
    @DisplayName("if createPaymentOrTransfer method was successfully then return CreatePaymentOrTransferResponseDto")
    void createPaymentOrTransfer_ifInformationClientNotSuccess_thenThrowRuntimeException() {
        //ARRANGE
        when(transferClient.createPaymentOrTransfer(any(UUID.class), any(CreatePaymentDto.class)))
                .thenReturn(ResponseEntity.ok(createPaymentResponseDto));
        when(informationClient.getClientData(any(UUID.class))).thenThrow(new RuntimeException());

        //ACT
        ThrowableAssert.ThrowingCallable createPaymentResponseDtoActual = () -> transferService
                .createPaymentOrTransfer(UUID.randomUUID(), new CreatePaymentDto());

        //VERIFY
        Assertions.assertThatThrownBy(createPaymentResponseDtoActual).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("if createPaymentOrTransfer method was successfully then return CreatePaymentOrTransferResponseDto")
    void createPaymentOrTransfer_ifTransferClientNotSuccess_thenThrowRuntimeException() {
        //ARRANGE
        when(transferClient.createPaymentOrTransfer(any(UUID.class), any(CreatePaymentDto.class)))
                .thenThrow(new RuntimeException());

        //ACT
        ThrowableAssert.ThrowingCallable createPaymentResponseDtoActual = () -> transferService
                .createPaymentOrTransfer(UUID.randomUUID(), new CreatePaymentDto());

        //VERIFY
        Assertions.assertThatThrownBy(createPaymentResponseDtoActual).isInstanceOf(RuntimeException.class);
    }

}