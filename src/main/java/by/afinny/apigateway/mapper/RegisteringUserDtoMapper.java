package by.afinny.apigateway.mapper;

import by.afinny.apigateway.dto.userservice.RegisteringUserDto;
import by.afinny.apigateway.dto.userservice.RequestRegisteringUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface RegisteringUserDtoMapper {
    @Mapping(source = "id",target = "id")
    RegisteringUserDto clientToUser(RequestRegisteringUserDto client, UUID id);
}
