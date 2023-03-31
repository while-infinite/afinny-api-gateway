package by.afinny.apigateway.openfeign.insurance;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest")
public interface DaDataApiClient {

    @PostMapping("address")
    String getAddressParameters(@RequestHeader HttpHeaders httpHeaders, @RequestBody String query);
}
