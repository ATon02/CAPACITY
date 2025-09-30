package co.com.backend.reactive.model.tecnologydata.gateways;

import co.com.backend.reactive.model.tecnologydata.TecnologyData;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;

public interface TecnologyDataRepository {
    Mono<TecnologyData> findById(Long id);
    Mono<Boolean> existsById(Long id);
    Flux<TecnologyData> findByIds(List<Long> ids);
    Mono<Void> deleteByIds(List<Long> ids);
}
