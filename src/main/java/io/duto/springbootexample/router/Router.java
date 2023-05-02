package io.duto.springbootexample.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Router {
    @Bean
    public RouterFunction<ServerResponse> home(WebRequestHandler requestHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/")
                .and(RequestPredicates.accept(MediaType.TEXT_HTML)),
                requestHandler::index);
    }

    @Bean
    public RouterFunction<ServerResponse> index(WebRequestHandler requestHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/index")
                        .and(RequestPredicates.accept(MediaType.TEXT_HTML)),
                requestHandler::index);
    }
}
