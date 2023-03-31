package by.afinny.apigateway.mapper.investments;

import by.afinny.apigateway.dto.investments.AvailableCurrenciesDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AvailableCurrenciesMapper {

    private final ObjectMapper mapper = new ObjectMapper();

    public AvailableCurrenciesDto.AllAvailableCurrencies toCurrenciesDtoList(String response) {

        List<AvailableCurrenciesDto> currencies = new ArrayList<>();

        try {
            JsonNode node = mapper.readTree(response);
            currencies = mapper.readValue(
                    mapper.writeValueAsString(node.get(1).withArray("marketdata")), new TypeReference<>(){}
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        AvailableCurrenciesDto.AllAvailableCurrencies allAvailableCurrenciesWrap = new AvailableCurrenciesDto.AllAvailableCurrencies();
        allAvailableCurrenciesWrap.setCurrencies(currencies);

        return allAvailableCurrenciesWrap;
    }

}
