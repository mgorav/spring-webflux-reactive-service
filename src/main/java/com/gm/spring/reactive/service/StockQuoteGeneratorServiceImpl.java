package com.gm.spring.reactive.service;

import com.gm.spring.reactive.domain.StockQuote;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

@Service
public class StockQuoteGeneratorServiceImpl implements StockQuoteGeneratorService {

    private final MathContext mathContext = new MathContext(2);
    private final Random random = new Random();
    private final List<StockQuote> prices = new ArrayList<>();

    public StockQuoteGeneratorServiceImpl() {
        this.prices.add(new StockQuote("APPLE", 187.13));
        this.prices.add(new StockQuote("MICROSOFT", 96.94));
        this.prices.add(new StockQuote("GOOGLE", 1083.75));
        this.prices.add(new StockQuote("ORACLE", 46.58));
        this.prices.add(new StockQuote("IBM", 141.61));
        this.prices.add(new StockQuote("INTEL", 54.34));
        this.prices.add(new StockQuote("REDHAT", 171.14));
        this.prices.add(new StockQuote("VMWARE", 138.57));
    }

    @Override
    public Flux<StockQuote> fetchQuoteStream(Duration period) {

        // We use here Flux.generate to create quotes,
        // iterating on each stock starting at index 0
        return Flux.generate(() -> 0,
                (BiFunction<Integer, SynchronousSink<StockQuote>, Integer>) (index, sink) -> {
                    StockQuote updatedStockQuote = updateQuote(this.prices.get(index));
                    sink.next(updatedStockQuote);
                    return ++index % this.prices.size();
                })
                // We want to emit them with a specific period;
                // to do so, we zip that Flux with a Flux.interval
                .zipWith(Flux.interval(period))
                .map(t -> t.getT1())
                // Because values are generated in batches,
                // we need to set their timestamp after their creation
                .map(quote -> {
                    quote.setInstant(Instant.now());
                    return quote;
                })
                .log("guru.springframework.service.QuoteGenerator");
    }

    private StockQuote updateQuote(StockQuote stockQuote) {
        BigDecimal priceChange = stockQuote.getPrice()
                .multiply(new BigDecimal(0.05 * this.random.nextDouble()), this.mathContext);
        return new StockQuote(stockQuote.getTicker(), stockQuote.getPrice().add(priceChange));
    }
}
