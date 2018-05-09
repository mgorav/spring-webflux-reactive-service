package com.gm.spring.reactive;

import com.gm.spring.reactive.domain.StockQuote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebfluxStockStockQuoteServiceApplicationTest {

    @Autowired //spring will inject a configured webTestClient
    private WebTestClient webTestClient;

    @Test
    public void testFetchQuotes() {
        webTestClient
                .get()
                .uri("/quotes?size=20") //set size = 20
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(StockQuote.class)
                .hasSize(20)
                .consumeWith(allQuotes -> {
                    assertThat(allQuotes.getResponseBody())
                            .allSatisfy(quote -> assertThat(quote.getPrice()).isPositive());

                    assertThat(allQuotes.getResponseBody()).hasSize(20);
                } );
    }

    @Test
    public void testStreamQuotes() throws InterruptedException {
        //set Countdown latch to 10
        CountDownLatch countDownLatch = new CountDownLatch(10);

        webTestClient
                .get()
                .uri("/quotes")
                .accept(APPLICATION_STREAM_JSON)
                .exchange()
                .returnResult(StockQuote.class)
                .getResponseBody()
                .take(10)
                .subscribe(quote -> {
                    assertThat(quote.getPrice()).isPositive();

                    countDownLatch.countDown();
                });

        countDownLatch.await();

        System.out.println("Test RUN Complete");

    }

}