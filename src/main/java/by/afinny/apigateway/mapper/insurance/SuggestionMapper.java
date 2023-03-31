package by.afinny.apigateway.mapper.insurance;

import by.afinny.apigateway.dto.insurance.SuggestionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SuggestionMapper {

    private final ObjectMapper mapper = new ObjectMapper();

    public SuggestionDto.Suggestions toSuggestionDtoList(String response) {

        List<JsonNode> jsonNodeList = new ArrayList<>();

        List<SuggestionDto> suggestions = new ArrayList<>();

        try {
            JsonNode node = mapper.readTree(response);
            node.get("suggestions").forEach(e -> jsonNodeList.add(e.get("data")));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for (JsonNode e: jsonNodeList) {
            suggestions.add(SuggestionDto.builder()
                    .region(e.get("region").asText())
                    .area(e.get("area").asText())
                    .city(e.get("city").asText())
                    .settlementWithType(e.get("settlement_with_type").asText())
                    .streetWithType(e.get("street_with_type").asText())
                    .house(e.get("house").asText())
                    .block(e.get("block").asText())
                    .flat(e.get("flat").asText())
                    .build());
        }

        SuggestionDto.Suggestions suggestionsWrap = new SuggestionDto.Suggestions();

        suggestionsWrap.setSuggestions(suggestions);

        return suggestionsWrap;
    }

}
