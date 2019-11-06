package com.example.spring.reactive.reactivespring.reactivetest;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author ArunKumar.Sugumar
 */
public class FluxAndMonoTest {

    @Test
    public void fluxTest() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("After error"))
                .log();
        fluxString.subscribe(System.out::println,
                System.out::println, () -> System.out.println("Completed"));
    }

    @Test
    public void fluxTestElementsWithoutError() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(fluxString)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxTestElementsCount() {

        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(fluxString)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void monoTestCount() {

        Mono<String> monoString = Mono.just("Spring")
                .log();

        StepVerifier.create(monoString)
                .expectNextCount(1)
                .verifyComplete();
    }
}
