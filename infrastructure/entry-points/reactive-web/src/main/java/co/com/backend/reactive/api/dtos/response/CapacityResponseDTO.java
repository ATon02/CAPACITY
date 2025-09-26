package co.com.backend.reactive.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapacityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Set<Long> technologies;
}