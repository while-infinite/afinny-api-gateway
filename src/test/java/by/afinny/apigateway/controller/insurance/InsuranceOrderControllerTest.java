package by.afinny.apigateway.controller.insurance;

import by.afinny.apigateway.dto.insurance.RequestMedicinePolicyDto;
import by.afinny.apigateway.dto.insurance.RequestNewPolicy;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyFromUser;
import by.afinny.apigateway.dto.insurance.RequestNewRealEstatePolicyToInsuranceService;
import by.afinny.apigateway.dto.insurance.SuggestionDto;
import by.afinny.apigateway.dto.insurance.RequestTravelPolicyDto;
import by.afinny.apigateway.dto.insurance.constant.DocumentType;
import by.afinny.apigateway.dto.insurance.constant.InsuranceCountry;
import by.afinny.apigateway.dto.insurance.constant.InsuranceStatus;
import by.afinny.apigateway.dto.insurance.constant.InsuranceType;
import by.afinny.apigateway.dto.insurance.constant.Period;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.dto.insurance.constant.SportType;
import by.afinny.apigateway.dto.userservice.ResponseClientDataDto;
import by.afinny.apigateway.mapper.InsuranceDtoMapper;
import by.afinny.apigateway.openfeign.insurance.InsuranceOrderClient;
import by.afinny.apigateway.service.insurance.InsuranceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsuranceOrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class InsuranceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceOrderClient insuranceOrderClient;

    @MockBean
    private InsuranceService insuranceService;

    @MockBean
    private InsuranceDtoMapper insuranceDtoMapper;


    private final String TEST_CLIENT_ID = "cc6588da-ffaf-4c00-a3bd-2e0c6d83655d";

    private final String TEST_ID = String.valueOf(UUID.randomUUID());

    private RequestNewPolicy requestNewPolicy;

    private RequestMedicinePolicyDto requestMedicinePolicyDto;

    private RequestNewRealEstatePolicyToInsuranceService toInsuranceService;

    private RequestNewRealEstatePolicyFromUser fromUser;

    private RequestTravelPolicyDto requestTravelPolicyDto;

    private ResponseClientDataDto responseClientDataDto;

    private SuggestionDto suggestionDto;

    private SuggestionDto.Suggestions suggestions;

    @BeforeEach
    void setUp() {
        requestNewPolicy = RequestNewPolicy.builder()
                .insuranceStatus("APPROVED")
                .registrationDate(LocalDate.of(2022, 10, 14))
                .startDate(LocalDate.of(2023, 10, 11))
                .periodOfInsurance("ONE_MONTH")
                .paymentCycle("THREE_MONTHS")
                .policySum(BigDecimal.valueOf(2))
                .region("Улица Нахимова дом 5")
                .model("model")
                .carNumber("2222")
                .isWithInsuredAccident(false)
                .categoryGroup("PASSENGER_AUTOMOBILE")
                .insuranceType("PROPERTY_INSURANCE")
                .capacityGroup("UP_TO_120_INCLUSIVE")
                .birthday(LocalDate.of(1993, 03, 13))
                .drivingExperience("FROM_5_TO_15_YEARS")
                .factorName("MIDDLE")
                .build();

        requestMedicinePolicyDto = RequestMedicinePolicyDto.builder()
                .birthday(LocalDate.of(2022, 5, 10))
                .region("region")
                .documentNumber("2")
                .documentType(DocumentType.INN)
                .insuranceSum(BigDecimal.valueOf(2))
                .policySum(BigDecimal.valueOf(2))
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .passportNumber("passportNumber")
                .email("email")
                .phoneNumber("phoneNumber")
                .programId(2)
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .registrationDate(LocalDate.of(2022, 5, 10))
                .periodOfInsurance(Period.SIX_MONTHS)
                .clientId("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb")
                .build();

        toInsuranceService = RequestNewRealEstatePolicyToInsuranceService.builder()
                .clientId("2222")
                .city("1")
                .district("1")
                .email("email")
                .firstName("1")
                .flatNumber("1")
                .houseNumber("1")
                .insuranceSum(BigDecimal.valueOf(2))
                .isFlat(true)
                .lastName("1")
                .middleName("1")
                .paymentCycle(String.valueOf(Period.THREE_MONTHS))
                .periodOfInsurance(String.valueOf(Period.ONE_MONTH))
                .phoneNumber("89992095590")
                .region("1")
                .registrationDate(LocalDate.of(2022, 11, 12))
                .street("1")
                .sumAssignmentName("FLAT_FURNITURE")
                .sumAssignmentSum(BigDecimal.valueOf(12))
                .policySum(BigDecimal.valueOf(2))
                .insuranceType("PROPERTY_INSURANCE")
                .sumAssignmentDefaultSum(BigDecimal.valueOf(30))
                .sumAssignmentMinSum(BigDecimal.valueOf(24))
                .sumAssignmentMaxSum(BigDecimal.valueOf(32))
                .sumAssignmentGeneralSum(BigDecimal.valueOf(30))
                .build();

        fromUser = RequestNewRealEstatePolicyFromUser.builder()
                .location("1")
                .insuranceSum(BigDecimal.valueOf(2))
                .paymentCycle(String.valueOf(Period.THREE_MONTHS))
                .periodOfInsurance(String.valueOf(Period.ONE_MONTH))
                .sumAssignmentName("FLAT_FURNITURE")
                .registrationDate(LocalDate.of(2022, 11, 12))
                .sumAssignmentSum(BigDecimal.valueOf(12))
                .policySum(BigDecimal.valueOf(2))
                .insuranceType("PROPERTY_INSURANCE")
                .sumAssignmentDefaultSum(BigDecimal.valueOf(30))
                .sumAssignmentMinSum(BigDecimal.valueOf(24))
                .sumAssignmentMaxSum(BigDecimal.valueOf(32))
                .sumAssignmentGeneralSum(BigDecimal.valueOf(30))
                .build();

        requestTravelPolicyDto = RequestTravelPolicyDto.builder()
                .insuranceCountry(InsuranceCountry.AUSTRIA)
                .birthday(LocalDate.of(2022, 5, 10))
                .sportType(SportType.SPORT)
                .insuranceSum(BigDecimal.valueOf(2))
                .policySum(BigDecimal.valueOf(2))
                .startDate(LocalDate.of(2022, 5, 10))
                .lastDate(LocalDate.of(2022, 6, 10))
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .passportNumber("passportNumber")
                .email("email")
                .phoneNumber("phoneNumber")
                .insuranceType(InsuranceType.PROPERTY_INSURANCE)
                .registrationDate(LocalDate.of(2022, 5, 10))
                .insuranceStatus(InsuranceStatus.PENDING)
                .clientId(TEST_CLIENT_ID)
                .travelProgramId(UUID.randomUUID())
                .build();

        responseClientDataDto = ResponseClientDataDto.builder()
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .email("email")
                .passportNumber("passportNumber")
                .clientId(TEST_CLIENT_ID)
                .build();
        suggestionDto = SuggestionDto.builder()
                .area("area")
                .block("block")
                .city("city")
                .flat("2")
                .house("2")
                .build();

    }



    @Test
    @DisplayName("The new insurance policy was successfully saved")
    void newInsurancePolicy_was_saved() throws Exception {
        //ARRANGE
        when(insuranceOrderClient.createNewPolicy(requestNewPolicy))
                .thenReturn(ResponseEntity.ok(requestNewPolicy));
        //ACT & VERIFY
        MvcResult result = mockMvc.perform(post("/api/v1/insurance-order/new-cars")
                        .contentType("application/json")
                        .content(asJsonString(requestNewPolicy)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If new insurance policy wasn't successfully saved then return status INTERNAL SERVER ERROR")
    void newInsurancePolicy_was_not_saved_thenStatus500() throws Exception {
        //ARRANGE
        doThrow(new RuntimeException())
                .when(insuranceOrderClient)
                .createNewPolicy(any(RequestNewPolicy.class));

        //ACT & VERIFY
        mockMvc.perform(
                        post("/api/v1/insurance-order/new-cars")
                                .contentType("application/json")
                                .content(asJsonString(requestNewPolicy)))
                .andExpect(status().isInternalServerError());
    }




    @ParameterizedTest
    @ValueSource(ints = {200, 401, 500})
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("If request for creating travel police then status should be redirected")
    void createNewTravelPolicy_shouldRedirectStatus(int httpStatus) throws Exception {
        //ARRANGE
        when(insuranceOrderClient.createNewTravelPolicy(any(RequestTravelPolicyDto.class)))
                .thenReturn(ResponseEntity.status(httpStatus).build());
        when(insuranceService.getUserInformation(any(Authentication.class))).thenReturn(responseClientDataDto);
        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/insurance-order/new-travel-program")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestTravelPolicyDto)))
                .andExpect(status().is(httpStatus));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_ID)
    @DisplayName("The new insurance policy was successfully saved")
    void newRealEstatePolicy_was_saved() throws Exception {
        //ARRANGE
        when(insuranceOrderClient.createNewRealEstatePolicy(any(RequestNewRealEstatePolicyToInsuranceService.class))).thenReturn(ResponseEntity.ok().build());

        //ACT & VERIFY
        mockMvc.perform(post("/api/v1/insurance-order/new-property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(fromUser)))
                .andExpect(status().isOk());

    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }
}
