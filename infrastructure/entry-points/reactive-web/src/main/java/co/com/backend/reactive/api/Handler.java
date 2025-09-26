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

}

