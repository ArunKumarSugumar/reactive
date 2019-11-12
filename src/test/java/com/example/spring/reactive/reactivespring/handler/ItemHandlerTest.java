package com.example.spring.reactive.reactivespring.handler;

import com.example.spring.reactive.reactivespring.document.Item;
import com.example.spring.reactive.reactivespring.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.example.spring.reactive.reactivespring.constants.ItemConstants.ITEM_FUNCTIONAL_ENDPOINT_V1;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ArunKumar.Sugumar
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ItemHandlerTest {


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data() {

        return Arrays.asList(new Item(null, "Samsung TV", 400.0),
                new Item(null, "LG TV", 420.0),
                new Item("Apple", "Apple TV", 299.0));
    }

    @BeforeEach
    public void setup() {

        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted item is: " + item))
                .blockLast();
    }


    @Test
    void getAllItems() {

        webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(3);
    }

    @Test
    void getAllItemsApproachTwo() {

        webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .consumeWith((response) -> {
                    response.getResponseBody().forEach(item -> {
                        assertTrue(item.getId() != null);
                    });
                });
    }

    @Test
    void getAllItemsApproachThree() {

        Flux<Item> responseBody = webTestClient.get().uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();
        StepVerifier.create(responseBody)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void saveItem() {

        webTestClient.post()
                .uri(ITEM_FUNCTIONAL_ENDPOINT_V1)
                .body(Mono.just(new Item(null, "BOSE Speaker", 1000.0)), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    void deleteItem() {

        webTestClient.delete()
                .uri(ITEM_FUNCTIONAL_ENDPOINT_V1.concat("/{id}"), "Apple")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    void updateItem() {

        webTestClient.put()
                .uri(ITEM_FUNCTIONAL_ENDPOINT_V1.concat("/{id}"), "Apple")
                .body(Mono.just(new Item("Apple", "Apple Ipad", 250.0)), Item.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 250.0);
    }
}
