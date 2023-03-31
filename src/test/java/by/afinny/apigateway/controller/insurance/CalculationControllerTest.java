package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.CoefficientsCalculationDto;
import by.afinny.apigateway.dto.insurance.FactorDto;
import by.afinny.apigateway.dto.insurance.ResponseFinalPriceDto;
import by.afinny.apigateway.dto.insurance.ResponseGeneralSumDto;
import by.afinny.apigateway.dto.insurance.ResponseSumAssignmentDto;
import by.afinny.apigateway.dto.insurance.constant.CapacityGroup;
import by.afinny.apigateway.dto.insurance.constant.CategoryGroup;
import by.afinny.apigateway.dto.insurance.constant.DrivingExperience;
import by.afinny.apigateway.dto.insurance.constant.ItemOfExpense;
import by.afinny.apigateway.openfeign.insurance.CalculationClient;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculationClient calculationClient;

    private List<ResponseGeneralSumDto> generalSumDtoList;

    private ResponseSumAssignmentDto sumAssignmentDto;

    private CoefficientsCalculationDto coefficientsCalculationDto;

    private ResponseFinalPriceDto expectedResponseFinalPriceDto;


    @BeforeEach
    void setUp() {
        generalSumDtoList = List.of(ResponseGeneralSumDto.builder()
                        .generalSum("100000.00").build(),
                ResponseGeneralSumDto.builder()
                        .generalSum("70000.00").build());

        sumAssignmentDto = ResponseSumAssignmentDto.builder()
                .id(String.valueOf(1))
                .name(String.valueOf(ItemOfExpense.FLAT_FURNITURE))
                .minSum(String.valueOf(new BigDecimal("90000.00")))
                .maxSum(String.valueOf(new BigDecimal("110000.00")))
                .defaultSum(String.valueOf(new BigDecimal("101000.00")))
                .build();

        coefficientsCalculationDto = CoefficientsCalculationDto.builder()
                .baseRate(7000)
                .factors(List.of(FactorDto.builder()
                        .factorName("HARD")
                        .factorRate(1.3)
                        .build(), FactorDto.builder()
                        .factorName("MIDDLE")
                        .factorRate(1.2)
                        .build(), FactorDto.builder()
                        .factorName("LIGHT")
                        .factorRate(1.1)
                        .build(), FactorDto.builder()
                        .factorName("HARD")
                        .factorRate(1.3)
                        .build()))
                .build();

        expectedResponseFinalPriceDto = ResponseFinalPriceDto.builder()
                .finalPrice(61190)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser
    @DisplayName("If request for getting general sum proceed then status should be redirected")
    void getGeneralSum_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(calculationClient.getGeneralSum(any(Boolean.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/calculations/property/sum")
                        .param("isFlat", String.valueOf(true)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting general sum proceed then response body should be redirected")
    void getGeneralSum_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(calculationClient.getGeneralSum(any(Boolean.class)))
                .thenReturn(ResponseEntity.ok(generalSumDtoList));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/calculations/property/sum")
                        .param("isFlat", String.valueOf(true)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(generalSumDtoList), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser
    @DisplayName("If request for getting sum assignment proceed then status should be redirected")
    void getSumAssignment_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(calculationClient.getSumAssignment(any(Boolean.class), any(BigDecimal.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/calculations/property")
                        .param("isFlat", String.valueOf(true))
                        .param("generalSum", String.valueOf(BigDecimal.valueOf(100000.00))))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting sum assignment proceed then response body should be redirected")
    void getSumAssignment_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(calculationClient.getSumAssignment(any(Boolean.class), any(BigDecimal.class)))
                .thenReturn(ResponseEntity.ok(sumAssignmentDto));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/calculations/property")
                        .param("isFlat", String.valueOf(true))
                        .param("generalSum", String.valueOf(BigDecimal.valueOf(100000.00))))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(sumAssignmentDto), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser
    @DisplayName("If request for getting coefficients proceed then status should be redirected")
    void getCoefficients_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(calculationClient.getCoefficients(any(String.class), any(CategoryGroup.class), any(CapacityGroup.class),
                any(Boolean.class), any(String.class), any(DrivingExperience.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/calculations/car")
                        .param("region", "Москва")
                        .param("categoryGroup", CategoryGroup.TRUCK.name())
                        .param("capacityGroup", CapacityGroup.OVER_120_TO_150_INCLUSIVE.name())
                        .param("isWithInsuredAccident", String.valueOf(true))
                        .param("birthday", "1995-01-01")
                        .param("drivingExperience", DrivingExperience.TILL_5_YEARS.name()))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting coefficients proceed then response body should be redirected")
    void getCoefficients_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(calculationClient.getCoefficients(any(String.class), any(CategoryGroup.class), any(CapacityGroup.class),
                any(Boolean.class), any(String.class), any(DrivingExperience.class)))
                .thenReturn(ResponseEntity.ok(coefficientsCalculationDto));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/calculations/car")
                        .param("region", "Москва")
                        .param("categoryGroup", CategoryGroup.TRUCK.name())
                        .param("capacityGroup", CapacityGroup.OVER_120_TO_150_INCLUSIVE.name())
                        .param("isWithInsuredAccident", String.valueOf(true))
                        .param("birthday", "1995-01-01")
                        .param("drivingExperience", DrivingExperience.TILL_5_YEARS.name()))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(coefficientsCalculationDto), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 500})
    @WithMockUser
    @DisplayName("If request for getting final price proceed then status should be redirected")
    void getFinalPrice_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(calculationClient.getFinalPrice(any(Boolean.class), any(Integer.class), any(Integer.class),
                any(String.class), any(String.class), any(Boolean.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        //ACT & VERIFY
        mockMvc.perform(get("/api/v1/calculations/travel")
                        .param("isWithSportType", String.valueOf(true))
                        .param("insuredNumber", String.valueOf(3))
                        .param("basicPrice", String.valueOf(1000))
                        .param("startDate", "11122022")
                        .param("lastDate", "21122022")
                        .param("isWithPCR", String.valueOf(true)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser
    @DisplayName("If request for getting final price proceed then response body should be redirected")
    void getFinalPrice_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(calculationClient.getFinalPrice(any(Boolean.class), any(Integer.class), any(Integer.class),
                any(String.class), any(String.class), any(Boolean.class))).thenReturn(ResponseEntity.ok(expectedResponseFinalPriceDto));
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/calculations/travel")
                        .param("isWithSportType", String.valueOf(true))
                        .param("insuredNumber", String.valueOf(3))
                        .param("basicPrice", String.valueOf(1000))
                        .param("startDate", "11122022")
                        .param("lastDate", "21122022")
                        .param("isWithPCR", String.valueOf(true)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(expectedResponseFinalPriceDto),mvcResult.getResponse().getContentAsString());
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
