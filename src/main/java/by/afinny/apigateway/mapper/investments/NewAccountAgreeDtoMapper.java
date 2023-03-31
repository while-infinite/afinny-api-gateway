package by.afinny.apigateway.mapper.investments;

import by.afinny.apigateway.dto.investments.RequestNewAccountAgreeDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NewAccountAgreeDtoMapper {

    @Mapping(source = "responseClientDataDto.mobilePhone", target = "phoneNumber")
    RequestNewAccountAgreeDto toRequestNewAccountAgree(ResponseClientDataDto responseClientDataDto);
}
