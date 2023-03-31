package by.afinny.apigateway.dto.userservice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@Schema(description = "DTO for response client")
public class ClientDto extends UserDto {

    @Schema(description = "client's id")
    private UUID id;
}

