package co.com.backend.reactive.api.mapper;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.api.dtos.request.CapacityRequestDTO;
import co.com.backend.reactive.api.dtos.response.CapacityResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapacityDTOMapper {
    
    Capacity toModel(CapacityRequestDTO requestDTO);
    
    CapacityResponseDTO toResponseDTO(Capacity capacity);
}