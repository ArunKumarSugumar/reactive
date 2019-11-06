package com.example.spring.reactive.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author ArunKumar.Sugumar
 */
@RunWith(SpringRunner.class)
@WebFluxTest
        //not work for components, service and database. Only for rest
class FluxMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxApproachOne() {

        Flux<Integer> responseBody = webTestClient.get().uri("/flux")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }

    @Test
    public void fluxApproachTwo() {

        webTestClient.get().uri("/flux")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void fluxApproachThree() {

        List<Integer> expectedList = Arrays.asList(1, 2, 3, 4);

        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get().uri("/flux")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedList, listEntityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxApproachFour() {

        List<Integer> expectedList = Arrays.asList(1, 2, 3, 4);

        webTestClient.get().uri("/flux")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((listEntityExchangeResult -> {
                    assertEquals(expectedList, listEntityExchangeResult.getResponseBody());
                }));
    }

    @Test
    public void fluxStreamApproachOne() {

        Flux<Long> responseBody = webTestClient.get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .thenCancel()
                .verify();
    }

    @Test
    public void monoApproachOne() {

        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((integerEntityExchangeResult -> {
                    assertEquals(Integer.valueOf(1), integerEntityExchangeResult.getResponseBody());
                }));
    }

}