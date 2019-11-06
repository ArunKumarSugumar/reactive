package com.example.spring.reactive.reactivespring.reactivetest;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author ArunKumar.Sugumar
 */
public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {

        Flux<Integer> range = Flux.range(1, 10);

        StepVerifier.create(range)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {

        Flux<Integer> range = Flux.range(1, 10)
                .log();

        range.subscribe((element) -> System.out.println("Element is " + element),
                (e) -> System.err.println("Exception is " + e),
                () -> System.out.println("Done"),
                (subscription -> subscription.request(2)));
    }
}
