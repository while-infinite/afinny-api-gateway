package by.afinny.apigateway.mapper.investmets;

import by.afinny.apigateway.dto.investments.AvailableStocksDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AvailableStocksMapper {

    private final ObjectMapper mapper = new ObjectMapper();

    public AvailableStocksDto.AllAvailableStocks toAllAvailableStocks(String jsonString) {

        List<AvailableStocksDto> availableStocks = new ArrayList<>();

        try {
            JsonNode node = mapper.readTree(jsonString);
            availableStocks = mapper.readValue(
                    mapper.writeValueAsString(node.get(1).withArray("marketdata")), new TypeReference<>() {
                    }
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        AvailableStocksDto.AllAvailableStocks allAvailableStocksWrap = new AvailableStocksDto.AllAvailableStocks();
        allAvailableStocksWrap.setAvailableStocksDtoList(availableStocks);
        return allAvailableStocksWrap;
    }
}
