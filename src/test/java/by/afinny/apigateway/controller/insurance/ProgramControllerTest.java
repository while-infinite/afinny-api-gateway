package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.ProgramDto;
import by.afinny.apigateway.dto.insurance.TravelProgramDto;
import by.afinny.apigateway.openfeign.insurance.ProgramClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProgramController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgramClient programClient;

    private List<ProgramDto> programDtoList;

    private List<TravelProgramDto> travelProgramDtoList;

    @BeforeEach
    void setUp() {
        programDtoList = List.of(ProgramDto.builder()
                .name("name")
                .sum(new BigDecimal("100000.00"))
                .organization("organization")
                .link("link")
                .description("description")
                .isEmergencyHospitalization(true)
                .isDentalService(true)
                .isTelemedicine(true)
                .isEmergencyMedicalCare(true)
                .isCallingDoctor(true)
                .isOutpatientService(true)
                .programId("1")
                .build());

        travelProgramDtoList = List.of(TravelProgramDto.builder()
                .name("classic")
                .description("classic insurance program")
                .finalPrice(new BigDecimal("100.11"))
                .maxInsuranceSum(new BigDecimal("10000."))
                .travelProgramId("1")
                .build());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 404, 500})
    @WithMockUser
    @DisplayName("If request for getting programs proceed then status should be redirected")
    void getGeneralSum_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(programClient.getMedicinePrograms(any(Integer.class), any(Integer.class), any(Boolean.class),
                any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/insurance-program/new-medicine/programs")
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(1))
                        .param("emergencyHospitalization", String.valueOf(true))
                        .param("dentalService", String.valueOf(true))
                        .param("telemedicine", String.valueOf(true))
                        .param("emergencyMedicalCare", String.valueOf(true))
                        .param("callingDoctor", String.valueOf(true))
                        .param("outpatientService", String.valueOf(true)))
                .andExpect(status().is(httpStatus));
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 400, 401, 404, 500})
    @WithMockUser
    @DisplayName("If request for getting travel programs proceed then status should be redirected")
    void getTravelPrograms_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(programClient.getTravelPrograms(any(Integer.class), any(Integer.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/insurance-program/new-travel-program/programs")
                .param("pageNumber", String.valueOf(0))
                .param("pageSize", String.valueOf(1)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting programs proceed then response body should be redirected")
    void getGeneralSum_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(programClient.getMedicinePrograms(any(Integer.class), any(Integer.class), any(Boolean.class),
                any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class)))
                .thenReturn(ResponseEntity.ok(programDtoList));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/insurance-program/new-medicine/programs")
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(1))
                        .param("emergencyHospitalization", String.valueOf(true))
                        .param("dentalService", String.valueOf(true))
                        .param("telemedicine", String.valueOf(true))
                        .param("emergencyMedicalCare", String.valueOf(true))
                        .param("callingDoctor", String.valueOf(true))
                        .param("outpatientService", String.valueOf(true)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(programDtoList), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting travel programs proceed then response body should be redirected")
    void getTravelPrograms_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(programClient.getTravelPrograms(any(Integer.class), any(Integer.class)))
                .thenReturn(ResponseEntity.ok(travelProgramDtoList));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/insurance-program/new-travel-program/programs")
                .param("pageNumber", String.valueOf(0))
                .param("pageSize", String.valueOf(1)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(travelProgramDtoList), mvcResult.getResponse().getContentAsString());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}
