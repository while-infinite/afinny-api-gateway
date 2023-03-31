package by.afinny.apigateway.service.moneytransfer.impl;

import by.afinny.apigateway.dto.moneytransfer.CreatePaymentResponseDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.mapper.moneytransfer.TransferMapper;
import by.afinny.apigateway.openfeign.moneytransfer.TransferClient;
import by.afinny.apigateway.openfeign.userservice.InformationClient;
import by.afinny.apigateway.service.moneytransfer.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferClient transferClient;
    private final InformationClient informationClient;
    private final TransferMapper transferMapper;

    @Override
    public CreatePaymentResponseDto createPaymentOrTransfer(UUID clientId, CreatePaymentDto createPaymentDto) {
        CreatePaymentResponseDto createPaymentResponseDto = Optional.of(transferClient.createPaymentOrTransfer(clientId, createPaymentDto).getBody())
                .orElseThrow(() -> new RuntimeException("Response from money-transfer service is empty"));

        ResponseClientDataDto clientData = Optional.of(informationClient.getClientData(clientId).getBody())
                .orElseThrow(() -> new RuntimeException("Response from user-service service is empty"));

        return transferMapper.toCreatePaymentResponseDto(createPaymentResponseDto, clientData);
    }
}
