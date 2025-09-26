package co.com.backend.reactive.api.config;

import co.com.backend.reactive.api.capacity.dto.CapacityRequestDTO;
import co.com.backend.reactive.api.capacity.dto.CapacityResponseDTO;
import co.com.backend.reactive.api.dtos.response.ErrorResponse;
import co.com.backend.reactive.api.dtos.response.BaseResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Tecnology API").version("v1"));
    }

    @Bean
    public GroupedOpenApi publicApi(OpenApiCustomizer customizer) {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(customizer)
                .build();
    }

    @Bean
    @Primary
    public OpenApiCustomizer customizer() {
        return openApi -> {
            openApi.getComponents()
                    .addSchemas("CapacityRequest", new Schema<CapacityRequestDTO>()
                            .addProperty("name", new StringSchema().maxLength(50))
                            .addProperty("description", new StringSchema().maxLength(90))
                            .addProperty("technologies", new Schema()))
                    .addSchemas("CapacityResponse", new Schema<CapacityResponseDTO>()
                            .addProperty("id", new StringSchema())
                            .addProperty("name", new StringSchema())
                            .addProperty("description", new StringSchema())
                            .addProperty("technologies", new Schema()))
                    .addSchemas("CapacitySuccessResponse", new Schema<BaseResponse<CapacityResponseDTO>>()
                            .addProperty("status", new IntegerSchema().format("int32"))
                            .addProperty("message", new StringSchema())
                            .addProperty("path", new StringSchema())
                            .addProperty("timestamp", new StringSchema().format("date-time"))
                            .addProperty("data", new Schema<>().$ref("#/components/schemas/CapacityResponse")))
                    .addSchemas("ErrorResponse", new Schema<ErrorResponse>()
                            .addProperty("status", new IntegerSchema().format("int32"))
                            .addProperty("message", new StringSchema())
                            .addProperty("path", new StringSchema())
                            .addProperty("timestamp", new StringSchema().format("date-time")));

            PathItem saveCapacityPath = new PathItem()
                    .post(new Operation()
                            .operationId("saveCapacity")
                            .tags(List.of("Capacity"))
                            .summary("Create a new capacity")
                            .requestBody(new RequestBody()
                                    .description("Capacity payload")
                                    .required(true)
                                    .content(new Content().addMediaType("application/json",
                                            new io.swagger.v3.oas.models.media.MediaType()
                                                    .schema(new Schema<>().$ref("#/components/schemas/CapacityRequest")))))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Saved capacity")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/CapacitySuccessResponse")))))
                                    .addApiResponse("400", new ApiResponse()
                                            .description("Validation error")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))))
                    );

            openApi.path("/api/capacity", saveCapacityPath);
        };
    }
}
