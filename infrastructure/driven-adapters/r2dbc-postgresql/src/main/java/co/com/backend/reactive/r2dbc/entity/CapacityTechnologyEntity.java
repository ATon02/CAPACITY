package co.com.backend.reactive.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("capacity_technology")
public class CapacityTechnologyEntity {
    @Id
    private Long id;
    private Long capacityId;
    private Long technologyId;
}