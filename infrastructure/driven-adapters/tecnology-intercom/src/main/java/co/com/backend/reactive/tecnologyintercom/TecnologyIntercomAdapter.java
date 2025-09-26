package co.com.backend.reactive.tecnologyintercom;

import co.com.backend.reactive.model.tecnologydata.TecnologyData;
import co.com.backend.reactive.model.tecnologydata.gateways.TecnologyDataRepository;
import co.com.backend.reactive.tecnologyintercom.dto.TechnologyIntercomResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.Set;

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

    public Flux<Boolean> validateTechnologies(Set<Long> technologyIds) {
        return Flux.fromIterable(technologyIds)
                .flatMap(this::existsById);
    }

}
