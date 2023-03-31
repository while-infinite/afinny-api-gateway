package by.afinny.apigateway.openfeign.insurance;

import by.afinny.apigateway.dto.insurance.CoefficientsCalculationDto;
import by.afinny.apigateway.dto.insurance.ResponseFinalPriceDto;
import by.afinny.apigateway.dto.insurance.ResponseGeneralSumDto;
import by.afinny.apigateway.dto.insurance.ResponseSumAssignmentDto;
import by.afinny.apigateway.dto.insurance.constant.CapacityGroup;
import by.afinny.apigateway.dto.insurance.constant.CategoryGroup;
import by.afinny.apigateway.dto.insurance.constant.DrivingExperience;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient("INSURANCE-SERVICE/auth/calculations")
public interface CalculationClient {

    @GetMapping("/property/sum")
    ResponseEntity<List<ResponseGeneralSumDto>> getGeneralSum(@RequestParam Boolean isFlat);

    @GetMapping("/property")
    ResponseEntity<ResponseSumAssignmentDto> getSumAssignment(@RequestParam Boolean isFlat,
                                                              @RequestParam BigDecimal generalSum);

    @GetMapping("/car")
    ResponseEntity<CoefficientsCalculationDto> getCoefficients(@RequestParam String region,
                                                               @RequestParam CategoryGroup categoryGroup,
                                                               @RequestParam CapacityGroup capacityGroup,
                                                               @RequestParam Boolean isWithInsuredAccident,
                                                               @RequestParam String birthday,
                                                               @RequestParam DrivingExperience drivingExperience);

    @GetMapping("/travel")
    ResponseEntity<ResponseFinalPriceDto> getFinalPrice(@RequestParam Boolean isWithSportType,
                                                        @RequestParam Integer insuredNumber,
                                                        @RequestParam Integer basicPrice,
                                                        @RequestParam String startDate,
                                                        @RequestParam String lastDate,
                                                        @RequestParam Boolean isWithPCR);
}
