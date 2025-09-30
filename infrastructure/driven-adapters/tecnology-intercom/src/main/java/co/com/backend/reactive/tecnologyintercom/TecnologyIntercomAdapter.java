package co.com.backend.reactive.tecnologyintercom;

import co.com.backend.reactive.model.tecnologydata.TecnologyData;
import co.com.backend.reactive.model.tecnologydata.gateways.TecnologyDataRepository;
import co.com.backend.reactive.tecnologyintercom.dto.TechnologyIntercomResponse;
import co.com.backend.reactive.tecnologyintercom.dto.TechnologyBatchIntercomResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TecnologyIntercomAdapter implements TecnologyDataRepository {

    private final WebClient webClient;

    public TecnologyIntercomAdapter(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8080").build();
    }

    @Override
    public Mono<TecnologyData> findById(Long id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/technologies/{id}")
                                             .build(id))
                .retrieve()
                .bodyToMono(TechnologyIntercomResponse.class)
                .filter(response -> response.getStatus() == 200 && response.getData() != null)
                .map(TechnologyIntercomResponse::getData)
                .onErrorResume(e -> {
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return findById(id)
                .map(technology -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Flux<TecnologyData> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Flux.empty();
        }
        
        String idsParam = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/technologies/batch")
                                             .queryParam("ids", idsParam)
                                             .build())
                .retrieve()
                .bodyToMono(TechnologyBatchIntercomResponse.class)
                .filter(response -> response.getStatus() == 200 && response.getData() != null)
                .flatMapMany(response -> Flux.fromIterable(response.getData()))
                .onErrorResume(e -> Flux.fromIterable(ids).flatMap(this::findById));
    }

    public Flux<Boolean> validateTechnologies(Set<Long> technologyIds) {
        return Flux.fromIterable(technologyIds)
                .flatMap(this::existsById);
    }

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.empty();
        }
        
        String idsParam = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/technologies")
                                             .queryParam("ids", idsParam)
                                             .build())
                .retrieve()
                .bodyToMono(TechnologyIntercomResponse.class)
                .filter(response -> response.getStatus() == 200)
                .then()
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Failed to delete technologies: " + e.getMessage(), e));
                });
    }

}
