package com.gm.spring.reactive.web.handler;

import com.gm.spring.reactive.domain.StockQuote;
import com.gm.spring.reactive.service.StockQuoteGeneratorService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
public class StockQuoteHandler {
    private final StockQuoteGeneratorService stockQuoteGeneratorService;

    public StockQuoteHandler(StockQuoteGeneratorService stockQuoteGeneratorService) {
        this.stockQuoteGeneratorService = stockQuoteGeneratorService;
    }

    public Mono<ServerResponse> fetchQuotes(ServerRequest request) {
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.stockQuoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100))
                        .take(size), StockQuote.class);
    }

    public Mono<ServerResponse> streamQuotes(ServerRequest request){
        return ok().contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.stockQuoteGeneratorService.fetchQuoteStream(Duration.ofMillis(200)), StockQuote.class);
    }
}
