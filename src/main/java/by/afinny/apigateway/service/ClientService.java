package by.afinny.apigateway.service;

import by.afinny.apigateway.dto.userservice.RegisteringUserDto;
import by.afinny.apigateway.dto.userservice.RequestRegisteringUserDto;

import java.util.UUID;


public interface ClientService {


    RegisteringUserDto registerExistingClient(UUID id, RequestRegisteringUserDto requestRegisteringUserDto);
}
