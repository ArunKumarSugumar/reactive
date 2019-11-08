package com.example.spring.reactive.reactivespring.repository;

import com.example.spring.reactive.reactivespring.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
            new Item("Apple", "Apple TV", 299.0));

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

    @Test
    public void getItemId() {

        Mono<Item> appleMono = itemReactiveRepository.findById("Apple");

        StepVerifier.create(appleMono)
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Apple TV"))
                .verifyComplete();
    }

    @Test
    public void findByDescription() {

        StepVerifier.create(itemReactiveRepository
                .findByDescription("Apple TV"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {

        Item item = new Item(null, "Google Mini", 30.0);
        Mono<Item> saveItem = itemReactiveRepository.save(item);

        StepVerifier.create(saveItem)
                .expectSubscription()
                .expectNextMatches(item1 -> item.getDescription().equals("Google Mini"))
                .verifyComplete();

    }

    @Test
    public void updateItem() {

        Mono<Item> lg_tv = itemReactiveRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(500.0);
                    return item;
                })
                .flatMap(item1 -> itemReactiveRepository.save(item1));

        StepVerifier.create(lg_tv)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 500.0)
                .verifyComplete();

    }

    @Test
    public void deleteItem() {

        Mono<Void> abc = itemReactiveRepository.findById("ABC")
                .map(Item::getId)
                .flatMap(id -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(abc)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }



}
