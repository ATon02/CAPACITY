package co.com.backend.reactive.api;

import co.com.backend.reactive.api.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler, Handler capacityHandler) {
        return route(POST("/api/capacity"), capacityHandler::createCapacity)
                .andRoute(GET("/api/capacity"), capacityHandler::getAllCapacitiesWithTechnologies);
    }
}
