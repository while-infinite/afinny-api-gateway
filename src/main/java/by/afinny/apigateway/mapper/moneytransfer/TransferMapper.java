package by.afinny.apigateway.mapper.moneytransfer;

import by.afinny.apigateway.dto.moneytransfer.CreatePaymentResponseDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransferMapper {

    @Mapping(target = "first_name", source = "clientData.firstName")
    @Mapping(target = "last_name", source = "clientData.lastName")
    @Mapping(target = "middle_name", source = "clientData.middleName")
    @Mapping(target = "mobile_phone", source = "clientData.mobilePhone")
    CreatePaymentResponseDto toCreatePaymentResponseDto(CreatePaymentResponseDto createPaymentResponseDto,
                                                        ResponseClientDataDto clientData);
}
