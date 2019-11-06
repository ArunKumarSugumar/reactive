package com.example.spring.reactive.reactivespring.reactivetest;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author ArunKumar.Sugumar
 */
public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergeFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("A", "B", "C","D", "E", "F")
                .verifyComplete();
    }
}
