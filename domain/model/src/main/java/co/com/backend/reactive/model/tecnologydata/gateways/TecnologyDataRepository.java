package co.com.backend.reactive.model.tecnologydata.gateways;

import co.com.backend.reactive.model.tecnologydata.TecnologyData;
import reactor.core.publisher.Mono;

public interface TecnologyDataRepository {
    Mono<TecnologyData> findById(Long id);
    Mono<Boolean> existsById(Long id);
}
