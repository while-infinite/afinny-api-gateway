package by.afinny.apigateway.openfeign.insurance;

import by.afinny.apigateway.dto.insurance.ProgramDto;
import by.afinny.apigateway.dto.insurance.TravelProgramDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient("INSURANCE-SERVICE/auth/insurance-program")
public interface ProgramClient {

    @GetMapping("/new-medicine/programs")
    ResponseEntity<List<ProgramDto>> getMedicinePrograms(@RequestParam Integer pageNumber,
                                                         @RequestParam Integer pageSize,
                                                         @RequestParam Boolean emergencyHospitalization,
                                                         @RequestParam Boolean dentalService,
                                                         @RequestParam Boolean telemedicine,
                                                         @RequestParam Boolean emergencyMedicalCare,
                                                         @RequestParam Boolean callingDoctor,
                                                         @RequestParam Boolean outpatientService);

    @GetMapping("/new-travel-program/programs")
    ResponseEntity<List<TravelProgramDto>> getTravelPrograms(@RequestParam Integer pageNumber,
                                                             @RequestParam Integer pageSize);
}
