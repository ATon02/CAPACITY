package co.com.backend.reactive.tecnologyintercom.dto;

import co.com.backend.reactive.model.tecnologydata.TecnologyData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyIntercomResponse {
    private int status;
    private String message;
    private String path;
    private String timestamp;
    private TecnologyData data;
}