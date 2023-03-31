package by.afinny.apigateway.config.security;

import by.afinny.apigateway.exception.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is responsible for generating a response to the failed user authentication.
 */
public class UnauthorizedErrorAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a response containing information about the failed authentication.
     *
     * @param request       http request.
     * @param response      http response.
     * @param authException authentication exception.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(
                objectMapper.writeValueAsString(
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .header("Content-Type", "application/json")
                                .body(new ErrorDto(Integer.toString(HttpStatus.UNAUTHORIZED.value()),
                                        authException.getMessage()))));
    }
}
