package by.afinny.apigateway.controller.credit;

import by.afinny.apigateway.dto.credit.ProductDto;
import by.afinny.apigateway.openfeign.credit.CreditProductClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/credit-products")
@Tag(name = "Product Controller", description = "Getting data about banking products")
public class CreditProductController {

    private final CreditProductClient creditProductClient;

    @Operation(summary = "Getting bank products", description = "Get information about bank products")
    @ApiResponse(responseCode = "200", description = "Found the products", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ProductDto.class))})
    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        return creditProductClient.getProducts();
    }
}
