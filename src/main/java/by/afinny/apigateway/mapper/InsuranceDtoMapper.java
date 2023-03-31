package by.afinny.apigateway.mapper;

import by.afinny.apigateway.dto.insurance.MedicinePolicyFromUserDto;
import by.afinny.apigateway.dto.insurance.NewPolicyFromUserDto;
import by.afinny.apigateway.dto.insurance.RequestMedicinePolicyDto;
import by.afinny.apigateway.dto.insurance.RequestNewPolicy;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyFromUser;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyToInsuranceService;
import by.afinny.apigateway.dto.insurance.SuggestionDto;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface InsuranceDtoMapper {
    @Mapping(source = "suggestions.region", target = "region")
    @Mapping(source = "responseClientDataDto.mobilePhone", target = "phoneNumber")
    RequestNewPolicy toPolicyDto(NewPolicyFromUserDto newPolicyFromUserDto, ResponseClientDataDto responseClientDataDto, SuggestionDto suggestions);

    @Mapping(source = "suggestionDto.house", target = "houseNumber")
    @Mapping(source = "suggestionDto.streetWithType", target = "street")
    @Mapping(source = "suggestionDto.flat", target = "flatNumber")
    @Mapping(source = "responseClientDataDto.mobilePhone", target = "phoneNumber")
    RequestNewRealEstatePolicyToInsuranceService toRealEstatePolicyToInsuranceService(RequestNewRealEstatePolicyFromUser estatePolicyFromUser,
                                                                                      SuggestionDto suggestionDto,
                                                                                      ResponseClientDataDto responseClientDataDto);


    @Mapping(source = "suggestions.region", target = "region")
    @Mapping(source = "responseClientDataDto.mobilePhone", target = "phoneNumber")
    RequestMedicinePolicyDto toMedicinePolicyDto(MedicinePolicyFromUserDto medicinePolicyFromUserDto,
                                                 ResponseClientDataDto responseClientDataDto, SuggestionDto suggestions);
}
