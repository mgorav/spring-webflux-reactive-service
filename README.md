##SpringWebfluxReactiveStockQuoteServiceApplication

An application demonstrates spring's reactive programing model and demonstrates:
1. MONO (Mono<ServerResponse>)
2. FLUX
3. ROUTERS (RouterFunction<ServerResponse> route(StockQuoteHandler handler))
4. HANDLERS
5. [Spring + MongoDB based reactive consumer of this service](https://lnkd.in/eFvdFUg)
6. [Project reactor](https://projectreactor.io)

####Tech Stack
1. Jdk 1.8+ (**JDK 1.9 Flux & Mono are not used**)
2. Spring BOOT 2.0.0.RELEASE (Spring 5.0.4 Webflux)
3. Lombok 1.16.20

#### Instruction to execute project
mvn clean install

#### Example URLS

1. http://localhost:9090/quotes?size=20
2. http://localhost:9090/quotes
