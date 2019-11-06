package com.example.spring.reactive.reactivespring.repository;

import com.example.spring.reactive.reactivespring.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/**
 * @author ArunKumar.Sugumar
 */

@DataMongoTest
@RunWith(SpringRunner.class)
class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    private List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Apple TV", 299.0));

    @BeforeEach
    public void setUp() {

        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> {
                    System.out.println("Inserted item is " + item);
                }).blockLast();
    }

    @Test
    public void getAllItems() {

        Flux<Item> items = itemReactiveRepository.findAll();

        StepVerifier.create(items)
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }
}
