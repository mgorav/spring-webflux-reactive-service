package com.gm.spring.reactive.web.router;


import com.gm.spring.reactive.web.handler.StockQuoteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class StockQuoteRouter {

    @Bean
    public RouterFunction<ServerResponse> route(StockQuoteHandler handler){
        return RouterFunctions
                .route(GET("/quotes").and(accept(APPLICATION_JSON)), handler::fetchQuotes)
                .andRoute(GET("/quotes").and(accept(APPLICATION_STREAM_JSON)), handler::streamQuotes);
    }
}
