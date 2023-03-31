package by.afinny.apigateway.controller.deposit;

import by.afinny.apigateway.dto.deposit.CardProductDto;
import by.afinny.apigateway.openfeign.deposit.CardProductsClient;
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
@RequestMapping("api/v1/cards-products")
@Tag(name = "Card Products Controller", description = "Manage actions with card products")
public class CardProductsController {

    private final CardProductsClient cardProductsClient;

    @Operation(summary = "Get all card products", description = "Get information about all card products of the bank")
    @ApiResponse(responseCode = "200", description = "Found the card products", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = CardProductDto.class))})
    @GetMapping()
    public ResponseEntity<List<CardProductDto>> getAllCardProducts() {
        return cardProductsClient.getAllCardProducts();
    }
}
