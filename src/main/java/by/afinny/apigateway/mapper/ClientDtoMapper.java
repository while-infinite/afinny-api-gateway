package by.afinny.apigateway.mapper;

import by.afinny.apigateway.dto.userservice.ClientDto;
import by.afinny.apigateway.dto.userservice.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface ClientDtoMapper {

  UserDto clientToUser(ClientDto client);
}
