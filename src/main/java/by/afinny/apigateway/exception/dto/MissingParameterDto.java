package by.afinny.apigateway.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MissingParameterDto {
    private final String parameterName;
    private final String message;
}
