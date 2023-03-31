package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.CoefficientsCalculationDto;
import by.afinny.apigateway.dto.insurance.ResponseFinalPriceDto;
import by.afinny.apigateway.dto.insurance.ResponseGeneralSumDto;
import by.afinny.apigateway.dto.insurance.ResponseSumAssignmentDto;
import by.afinny.apigateway.dto.insurance.constant.CapacityGroup;
import by.afinny.apigateway.dto.insurance.constant.CategoryGroup;
import by.afinny.apigateway.dto.insurance.constant.DrivingExperience;
import by.afinny.apigateway.openfeign.insurance.CalculationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/calculations")
@RequiredArgsConstructor
@Tag(name = "Calculation Controller", description = "Manage actions with sum assignment")
public class CalculationController {
    private final CalculationClient calculationClient;

    @Operation(summary = "Getting list of general sum", description = "Get information about  list of general sum")
    @ApiResponse(responseCode = "200",
                 description = "Found general sum list",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseGeneralSumDto.class))})
    @GetMapping("/property/sum")
    public ResponseEntity<List<ResponseGeneralSumDto>> getGeneralSum(@RequestParam Boolean isFlat) {
        return calculationClient.getGeneralSum(isFlat);
    }

    @Operation(summary = "Getting sum assignment", description = "Get information about sum assignment")
    @ApiResponse(responseCode = "200",
                 description = "Found sum assignment",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseSumAssignmentDto.class))})
    @GetMapping("/property")
    public ResponseEntity<ResponseSumAssignmentDto> getSumAssignment(@RequestParam Boolean isFlat,
                                                                     @RequestParam BigDecimal generalSum) {
        return calculationClient.getSumAssignment(isFlat, generalSum);
    }

    @Operation(summary = "Getting coefficients", description = "Get coefficients for calculating car insurance policy")
    @ApiResponse(responseCode = "200",
                 description = "Found coefficients",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = CoefficientsCalculationDto.class))})
    @GetMapping("/car")
    public ResponseEntity<CoefficientsCalculationDto> getCoefficients(@RequestParam String region,
                                                                      @RequestParam CategoryGroup categoryGroup,
                                                                      @RequestParam CapacityGroup capacityGroup,
                                                                      @RequestParam Boolean isWithInsuredAccident,
                                                                      @RequestParam String birthday,
                                                                      @RequestParam DrivingExperience drivingExperience) {
        return calculationClient.getCoefficients(region, categoryGroup, capacityGroup, isWithInsuredAccident, birthday, drivingExperience);
    }

    @Operation(summary = "Getting final price", description = "Get final price of travel insurance policy")
    @ApiResponse(responseCode = "200",
                 description = "Found final price",
                 content = {@Content(mediaType = "application/json",
                         schema = @Schema(implementation = ResponseFinalPriceDto.class))})
    @GetMapping("/travel")
    public ResponseEntity<ResponseFinalPriceDto> getFinalPrice(@RequestParam Boolean isWithSportType,
                                                               @RequestParam Integer insuredNumber,
                                                               @RequestParam Integer basicPrice,
                                                               @RequestParam String startDate,
                                                               @RequestParam String lastDate,
                                                               @RequestParam Boolean isWithPCR) {
        return calculationClient.getFinalPrice(isWithSportType, insuredNumber, basicPrice, startDate, lastDate, isWithPCR);
    }
}
