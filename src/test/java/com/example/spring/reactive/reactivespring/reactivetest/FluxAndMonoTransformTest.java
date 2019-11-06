package com.example.spring.reactive.reactivespring.reactivetest;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/**
 * @author ArunKumar.Sugumar
 */
public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("Arun", "Kumar", "Jack", "King");

    @Test
    public void transformUsingMap() {

        Flux<String> fluxString = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(fluxString)
                .expectNext("ARUN", "KUMAR", "JACK", "KING")
                .verifyComplete();

    }

    @Test
    public void transformUsingMap_Length() {

        Flux<Integer> fluxString = Flux.fromIterable(names)
                .map(s -> s.length())
                .log();

        StepVerifier.create(fluxString)
                .expectNext(4, 5, 4, 4)
                .verifyComplete();

    }
}
