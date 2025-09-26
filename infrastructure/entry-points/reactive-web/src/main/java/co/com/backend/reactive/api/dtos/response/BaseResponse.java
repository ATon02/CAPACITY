package co.com.backend.reactive.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private T data;
}
