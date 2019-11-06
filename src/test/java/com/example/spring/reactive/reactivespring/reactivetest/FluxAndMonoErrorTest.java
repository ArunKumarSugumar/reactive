package com.example.spring.reactive.reactivespring.reactivetest;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author ArunKumar.Sugumar
 */
public class FluxAndMonoErrorTest {

    @Test
    public void errorHandling(){

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    System.out.println("Exception is "+ e);
                    return Flux.just("default", "default1");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                /*.expectError(RuntimeException.class)
                .verify();*/
                .expectNext("default", "default1")
                .verifyComplete();
    }
}
