package co.com.backend.reactive.tecnologyintercom.dto;

import co.com.backend.reactive.model.tecnologydata.TecnologyData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyBatchIntercomResponse {
    private int status;
    private String message;
    private String path;
    private String timestamp;
    private List<TecnologyData> data;
}