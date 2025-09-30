package co.com.backend.reactive.api;

import co.com.backend.reactive.usecase.capacity.ICapacityUseCase;
import co.com.backend.reactive.usecase.capacity.dto.CapacityCompletedResponse;
import co.com.backend.reactive.api.dtos.request.CapacityRequestDTO;
import co.com.backend.reactive.api.dtos.response.CapacityResponseDTO;
import co.com.backend.reactive.api.dtos.response.BaseResponse;
import co.com.backend.reactive.api.mapper.CapacityDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {
    private final ICapacityUseCase capacityUseCase;
    private final CapacityDTOMapper capacityDTOMapper;

    public Mono<ServerResponse> createCapacity(ServerRequest serverRequest) {
        String path = serverRequest.path();
        
        return serverRequest.bodyToMono(CapacityRequestDTO.class)
                .map(capacityDTOMapper::toModel)
                .flatMap(capacityUseCase::save)
                .map(capacityDTOMapper::toResponseDTO)
                .flatMap(capacity -> {
                    BaseResponse<CapacityResponseDTO> body = BaseResponse.<CapacityResponseDTO>builder()
                            .status(200)
                            .message("Capacity created successfully")
                            .path(path)
                            .timestamp(LocalDateTime.now())
                            .data(capacity)
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                });
    }

    public Mono<ServerResponse> getAllCapacitiesWithTechnologies(ServerRequest serverRequest) {
        String path = serverRequest.path();
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        String sort = serverRequest.queryParam("sort").orElse("name,asc");
        
        String[] sortParts = sort.split(",");
        String sortBy = sortParts.length > 0 ? sortParts[0] : "name";
        String sortDirection = sortParts.length > 1 ? sortParts[1] : "asc";

        return capacityUseCase.getAllCapacitiesWithTechnologies(page, size, sortBy, sortDirection)
                .collectList()
                .flatMap(response -> {
                    BaseResponse<List<CapacityCompletedResponse>> body = 
                            BaseResponse.<List<CapacityCompletedResponse>>builder()
                            .status(200)
                            .message("Capacities retrieved successfully")
                            .path(path)
                            .timestamp(LocalDateTime.now())
                            .data(response)
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                });
    }

    public Mono<ServerResponse> getCapacityById(ServerRequest request) {
        String idCapacity = request.pathVariable("id");

        return Mono.fromCallable(() -> Long.parseLong(idCapacity))
                .onErrorMap(NumberFormatException.class,
                        ex -> new IllegalArgumentException("Invalid capacity ID format"))
                .flatMap(capacityUseCase::findById)
                .map(capacityDTOMapper::toResponseDTO)
                .flatMap(capacity -> {
                    BaseResponse<CapacityResponseDTO> body = BaseResponse.<CapacityResponseDTO>builder()
                            .status(200)
                            .message("Capacity found successfully")
                            .path(request.path())
                            .timestamp(LocalDateTime.now())
                            .data(capacity)
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                });
    }

    public Mono<ServerResponse> getCapacityByIds(ServerRequest request) {
        String idsParam = request.queryParam("ids").orElse("");
        return Mono.fromCallable(() -> {
                    if (idsParam.isEmpty()) {
                        throw new IllegalArgumentException("IDs parameter is required");
                    }
                    return Arrays.stream(idsParam.split(","))
                            .map(String::trim)
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                })
                .onErrorMap(NumberFormatException.class,
                        ex -> new IllegalArgumentException("Invalid ID format in parameters"))
                .flatMapMany(capacityUseCase::findByIds)
                .collectList()
                .flatMap(response -> {
                    BaseResponse<List<CapacityCompletedResponse>> body = 
                            BaseResponse.<List<CapacityCompletedResponse>>builder()
                            .status(200)
                            .message("Capacities retrieved successfully")
                            .path(request.path())
                            .timestamp(LocalDateTime.now())
                            .data(response)
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                });
    }

    public Mono<ServerResponse> deleteCapacities(ServerRequest request) {
        String idsParam = request.queryParam("ids").orElse("");
        return Mono.fromCallable(() -> {
                    if (idsParam.isEmpty()) {
                        throw new IllegalArgumentException("IDs parameter is required");
                    }
                    return Arrays.stream(idsParam.split(","))
                            .map(String::trim)
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                })
                .onErrorMap(NumberFormatException.class, 
                    ex -> new IllegalArgumentException("Invalid ID format in parameters"))
                .flatMap(capacityUseCase::deleteCapacities)
                .then(Mono.defer(() -> {
                    BaseResponse<Void> body = BaseResponse.<Void>builder()
                            .status(200)
                            .message("Capacities deleted successfully")
                            .path(request.path())
                            .timestamp(LocalDateTime.now())
                            .data(null)
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                }));
    }

}

