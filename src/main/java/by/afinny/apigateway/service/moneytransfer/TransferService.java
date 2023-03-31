package by.afinny.apigateway.service.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CreatePaymentDto;
import by.afinny.apigateway.dto.moneytransfer.CreatePaymentResponseDto;

import java.util.UUID;

public interface TransferService {
    CreatePaymentResponseDto createPaymentOrTransfer(UUID clientId, CreatePaymentDto createPaymentDto);
}
