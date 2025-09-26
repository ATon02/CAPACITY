package co.com.backend.reactive.model.capacity;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Capacity {
    private Long id;
    private String name;
    private String description;
    
    @Builder.Default
    private Set<Long> technologies = new HashSet<>();
}
