package co.com.backend.reactive.api.config;

import co.com.backend.reactive.api.dtos.request.CapacityRequestDTO;
import co.com.backend.reactive.api.dtos.response.CapacityResponseDTO;
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
        return new OpenAPI().info(new Info().title("Capacity Management API").version("v1"));
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
                            .addProperty("technologies", new Schema<>().type("array")
                                    .items(new Schema<>().type("integer").format("int64"))))
                    .addSchemas("TechnologyDTO", new Schema<>()
                            .addProperty("id", new Schema<>().type("integer").format("int64"))
                            .addProperty("name", new StringSchema()))
                    .addSchemas("CapacityResponse", new Schema<CapacityResponseDTO>()
                            .addProperty("id", new Schema<>().type("integer").format("int64"))
                            .addProperty("name", new StringSchema())
                            .addProperty("description", new StringSchema())
                            .addProperty("technologies", new Schema<>().type("array")
                                    .items(new Schema<>().$ref("#/components/schemas/TechnologyDTO"))))
                    .addSchemas("CapacitySuccessResponse", new Schema<BaseResponse<CapacityResponseDTO>>()
                            .addProperty("status", new IntegerSchema().format("int32"))
                            .addProperty("message", new StringSchema())
                            .addProperty("path", new StringSchema())
                            .addProperty("timestamp", new StringSchema().format("date-time"))
                            .addProperty("data", new Schema<>().$ref("#/components/schemas/CapacityResponse")))
                    .addSchemas("CapacityListSuccessResponse", new Schema<>()
                            .addProperty("status", new IntegerSchema().format("int32"))
                            .addProperty("message", new StringSchema())
                            .addProperty("path", new StringSchema())
                            .addProperty("timestamp", new StringSchema().format("date-time"))
                            .addProperty("data", new Schema<>().type("array")
                                    .items(new Schema<>().$ref("#/components/schemas/CapacityResponse"))))
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
                    )
                    .get(new Operation()
                            .operationId("getAllCapacitiesWithTechnologies")
                            .tags(List.of("Capacity"))
                            .summary("Get all capacities with technologies paginated")
                            .description("Retrieves all capacities along with their associated technologies, with pagination and sorting support")
                            .addParametersItem(new Parameter()
                                    .name("page")
                                    .in("query")
                                    .required(false)
                                    .description("Page number (starts at 0)")
                                    .schema(new IntegerSchema().minimum(java.math.BigDecimal.ZERO)._default(0)))
                            .addParametersItem(new Parameter()
                                    .name("size")
                                    .in("query")
                                    .required(false)
                                    .description("Number of items per page")
                                    .schema(new IntegerSchema().minimum(java.math.BigDecimal.ONE).maximum(java.math.BigDecimal.valueOf(100))._default(10)))
                            .addParametersItem(new Parameter()
                                    .name("sort")
                                    .in("query")
                                    .required(false)
                                    .description("Sort field and direction. Format: field,direction. Valid fields: 'name', 'tech_count'. Direction: 'asc', 'desc'. Example: 'name,asc' or 'tech_count,desc'")
                                    .schema(new StringSchema()._default("name,asc")))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Capacities with technologies retrieved successfully")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/CapacityListSuccessResponse")))))
                                    .addApiResponse("400", new ApiResponse()
                                            .description("Invalid pagination or sorting parameters")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))))
                    )
                    .delete(new Operation()
                            .operationId("deleteCapacities")
                            .tags(List.of("Capacity"))
                            .summary("Delete capacities by IDs")
                            .description("Deletes multiple capacities by providing a list of IDs as query parameter. Also removes orphaned technologies and all related capacity-technology relationships.")
                            .addParametersItem(new Parameter()
                                    .name("ids")
                                    .in("query")
                                    .required(true)
                                    .description("Comma-separated list of capacity IDs to delete. Example: ids=1,2,3")
                                    .schema(new StringSchema()))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Capacities deleted successfully")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>()
                                                                    .addProperty("status", new IntegerSchema().format("int32"))
                                                                    .addProperty("message", new StringSchema())
                                                                    .addProperty("path", new StringSchema())
                                                                    .addProperty("timestamp", new StringSchema().format("date-time"))
                                                                    .addProperty("data", new Schema<>().type("object").nullable(true))))))
                                    .addApiResponse("400", new ApiResponse()
                                            .description("Invalid IDs format, missing IDs parameter, or invalid ID values")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                                    .addApiResponse("404", new ApiResponse()
                                            .description("One or more capacities not found")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))))
                    );

            PathItem getCapacityByIdPath = new PathItem()
                    .get(new Operation()
                            .operationId("getCapacityById")
                            .tags(List.of("Capacity"))
                            .summary("Get capacity by ID")
                            .description("Retrieves a specific capacity by its ID")
                            .addParametersItem(new Parameter()
                                    .name("id")
                                    .in("path")
                                    .required(true)
                                    .description("Capacity ID")
                                    .schema(new Schema<>().type("integer").format("int64")))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Capacity found successfully")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/CapacitySuccessResponse")))))
                                    .addApiResponse("400", new ApiResponse()
                                            .description("Invalid capacity ID format or invalid ID")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                                    .addApiResponse("404", new ApiResponse()
                                            .description("Capacity not found")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))))
                    );

            PathItem getCapacitiesByIdsPath = new PathItem()
                    .get(new Operation()
                            .operationId("getCapacityByIds")
                            .tags(List.of("Capacity"))
                            .summary("Get capacities by list of IDs")
                            .description("Retrieves multiple capacities by providing a list of IDs as query parameter")
                            .addParametersItem(new Parameter()
                                    .name("ids")
                                    .in("query")
                                    .required(true)
                                    .description("Comma-separated list of capacity IDs. Example: ids=1,2,3")
                                    .schema(new StringSchema()))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Capacities found successfully")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/CapacityListSuccessResponse")))))
                                    .addApiResponse("400", new ApiResponse()
                                            .description("Invalid IDs format, missing IDs parameter, or invalid ID values")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                                    .addApiResponse("404", new ApiResponse()
                                            .description("No capacities found for the provided IDs")
                                            .content(new Content().addMediaType("application/json",
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))))
                    );

            openApi.path("/api/capacity", saveCapacityPath);
            openApi.path("/api/v1/capacity/{id}", getCapacityByIdPath);
            openApi.path("/api/v1/capacity/batch", getCapacitiesByIdsPath);
        };
    }
}
