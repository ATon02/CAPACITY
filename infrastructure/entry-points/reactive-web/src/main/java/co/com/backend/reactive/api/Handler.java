package co.com.backend.reactive.api;

import co.com.backend.reactive.usecase.capacity.CapacityUseCase;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class Handler {
    private final CapacityUseCase capacityUseCase;
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
                    BaseResponse<List<co.com.backend.reactive.usecase.capacity.dto.CapacityResponseDTO>> body = 
                            BaseResponse.<List<co.com.backend.reactive.usecase.capacity.dto.CapacityResponseDTO>>builder()
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

}

