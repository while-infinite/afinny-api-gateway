package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.ProductDto;
import by.afinny.apigateway.openfeign.deposit.DepositProductClient;
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
@RequestMapping("api/v1/deposit-products")
@Tag(name = "Product Controller", description = "Getting data about deposit products")
public class DepositProductController {

    private final DepositProductClient depositProductClient;

    @Operation(summary = "Get active deposit products", description = "Get information about all active deposit products")
    @ApiResponse(responseCode = "200", description = "Products were found", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ProductDto.class))})
    @GetMapping
    public ResponseEntity<List<ProductDto>> getActiveDepositProducts() {
        return depositProductClient.getActiveDepositProducts();
    }
}