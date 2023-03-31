package by.afinny.apigateway.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userServiceApi() {
        return GroupedOpenApi.builder()
                .group("user-service")
                .packagesToScan("by.afinny.apigateway.controller.userservice")
                .build();
    }

    @Bean
    public GroupedOpenApi creditServiceApi() {
        return GroupedOpenApi.builder()
                .group("credit-service")
                .packagesToScan("by.afinny.apigateway.controller.credit")
                .build();
    }

    @Bean
    public GroupedOpenApi depositServiceApi() {
        return GroupedOpenApi.builder()
                .group("deposit-service")
                .packagesToScan("by.afinny.apigateway.controller.deposit")
                .build();
    }

    @Bean
    public GroupedOpenApi exchangeRateServiceApi() {
        return GroupedOpenApi.builder()
                .group("info-service")
                .packagesToScan("by.afinny.apigateway.controller.infoservice")
                .build();
    }

    @Bean
    public GroupedOpenApi insuranceServiceApi() {
        return GroupedOpenApi.builder()
                .group("insurance-service")
                .packagesToScan("by.afinny.apigateway.controller.insurance")
                .build();
    }

    @Bean
    public GroupedOpenApi moneyTransferServiceApi() {
        return GroupedOpenApi.builder()
                .group("money-transfer")
                .packagesToScan("by.afinny.apigateway.controller.moneytransfer")
                .build();
    }

    @Bean
    public GroupedOpenApi investmentServiceApi() {
        return GroupedOpenApi.builder()
                .group("investment-service")
                .packagesToScan("by.afinny.apigateway.controller.investments")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(
            @Value("${APPLICATION_NAME:API-GATEWAY}") String appName,
            @Value("${APPLICATION_DESCRIPTION:A-Finny - banking application}") String appDescription,
            @Value("${APPLICATION_VERSION: 0.0.1-SNAPSHOT}") String appVersion,
            @Value("${SERVER_URL}") String serverUrl) {

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"))
                .components(new Components().addSecuritySchemes("ApiKeyAuth",
                        new SecurityScheme()
                                .name("Authorization")
                                .in(In.HEADER)
                                .type(Type.APIKEY)))
                .info(new Info().title(appName)
                        .version(appVersion)
                        .description(appDescription))
                .servers(List.of(new Server().url(serverUrl).description("api-gateway port: DEV")));
    }
}
