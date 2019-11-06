package com.example.spring.reactive.reactivespring.reactivetest;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author ArunKumar.Sugumar
 */
public class FluxAndMonoFactoryTest {

    private List<String> names = Arrays.asList("Arun", "Kumar", "Jack", "King");

    @Test
    public void fluxUsingIterable() {

        Flux<String> namesFlux = Flux.fromIterable(names);

        StepVerifier.create(namesFlux)
                .expectNext("Arun", "Kumar", "Jack", "King")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {

        String[] names = new String[]{"Arun", "Kumar", "Jack", "King"};

        Flux<String> namesFlux = Flux.fromArray(names);

        StepVerifier.create(namesFlux)
                .expectNext("Arun", "Kumar", "Jack", "King")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {

        Flux<String> namesFlux = Flux.fromStream(names.stream());

        StepVerifier.create(namesFlux)
                .expectNext("Arun", "Kumar", "Jack", "King")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {

        Mono<String> mono = Mono.justOrEmpty(null);

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {

        Supplier<String> stringSupplier = () -> "Arun";

        Mono<String> mono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(mono)
                .expectNext("Arun")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {

        Flux<Integer> integerFlux = Flux.range(1, 5);

        StepVerifier.create(integerFlux)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }
}
