package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.ProgramDto;
import by.afinny.apigateway.dto.insurance.TravelProgramDto;
import by.afinny.apigateway.openfeign.insurance.ProgramClient;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/insurance-program")
@Tag(name = "Insurance Program Controller", description = "Getting data about insurance-programs")
public class ProgramController {

    private final ProgramClient programClient;

    @Operation(summary = "Getting medicine programs", description = "Get information about medicine programs")
    @ApiResponse(responseCode = "200",
                 description = "Found medicine programs",
                 content = {@Content(mediaType = "application/json",
                 schema = @Schema(implementation = List.class))})
    @GetMapping("/new-medicine/programs")
    public ResponseEntity<List<ProgramDto>> getMedicinePrograms(@RequestParam Integer pageNumber,
                                                                @RequestParam Integer pageSize,
                                                                @RequestParam Boolean emergencyHospitalization,
                                                                @RequestParam Boolean dentalService,
                                                                @RequestParam Boolean telemedicine,
                                                                @RequestParam Boolean emergencyMedicalCare,
                                                                @RequestParam Boolean callingDoctor,
                                                                @RequestParam Boolean outpatientService) {
        return programClient.getMedicinePrograms(pageNumber,
                                                 pageSize,
                                                 emergencyHospitalization,
                                                 dentalService,
                                                 telemedicine,
                                                 emergencyMedicalCare,
                                                 callingDoctor,
                                                 outpatientService);
    }

    @Operation(summary = "Getting travel programs", description = "Get information about travel programs")
    @ApiResponse(responseCode = "200",
                 description = "Found travel programs",
                 content = {@Content(mediaType = "application/json",
                 schema = @Schema(implementation = List.class))})
    @GetMapping("/new-travel-program/programs")
    public ResponseEntity<List<TravelProgramDto>> getTravelPrograms(@RequestParam Integer pageNumber,
                                                                    @RequestParam(defaultValue = "4") Integer pageSize) {
        return programClient.getTravelPrograms(pageNumber,
                                               pageSize);
    }
}
