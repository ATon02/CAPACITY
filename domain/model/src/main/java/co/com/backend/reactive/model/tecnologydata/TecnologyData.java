package co.com.backend.reactive.model.tecnologydata;
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
public class TecnologyData {
    private Long id;
    private String name;
    private String description;
}
