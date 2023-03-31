package by.afinny.apigateway.service.insurance.impl;

import by.afinny.apigateway.dto.userservice.RegisteringUserDto;
import by.afinny.apigateway.dto.userservice.RequestRegisteringUserDto;
import by.afinny.apigateway.mapper.RegisteringUserDtoMapper;
import by.afinny.apigateway.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final RegisteringUserDtoMapper registeringUserDtoMapper;


    @Override
    public RegisteringUserDto registerExistingClient(UUID id, RequestRegisteringUserDto requestRegisteringUserDto) {
        return registeringUserDtoMapper.clientToUser(requestRegisteringUserDto, id);
    }
}
