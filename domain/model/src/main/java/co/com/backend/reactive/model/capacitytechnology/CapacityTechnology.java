package co.com.backend.reactive.model.capacitytechnology;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacityTechnology {
    private Long id;
    private Long capacityId;
    private Long technologyId;
}
