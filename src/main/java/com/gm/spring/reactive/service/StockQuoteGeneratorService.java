package com.gm.spring.reactive.service;

import com.gm.spring.reactive.domain.StockQuote;
import reactor.core.publisher.Flux;

import java.time.Duration;

public interface StockQuoteGeneratorService {

    Flux<StockQuote> fetchQuoteStream(Duration period);
}
