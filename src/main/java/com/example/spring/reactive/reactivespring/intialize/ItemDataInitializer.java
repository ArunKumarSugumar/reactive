package com.example.spring.reactive.reactivespring.intialize;

import com.example.spring.reactive.reactivespring.document.Item;
import com.example.spring.reactive.reactivespring.document.ItemCapped;
import com.example.spring.reactive.reactivespring.repository.ItemReactiveCappedRepository;
import com.example.spring.reactive.reactivespring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @author ArunKumar.Sugumar
 */
@Component
@Profile("!test")
@Slf4j
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    ItemReactiveCappedRepository itemReactiveCappedRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void run(String... args) throws Exception {

        initialDataSetup();
        createCappedCollection();
        dataSetupForCappedCollection();
    }

    private void createCappedCollection() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
    }

    public List<Item> data() {

        return Arrays.asList(new Item(null, "Samsung TV", 400.0),
                new Item(null, "LG TV", 420.0),
                new Item("Apple", "Apple TV", 299.0));
    }

    private void initialDataSetup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("item inserted from command line runner: " + item));

    }

    private void dataSetupForCappedCollection() {
        Flux<ItemCapped> map = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "Random Item " + i, 100.00 + i));
        itemReactiveCappedRepository.insert(map)
                .subscribe(itemCapped -> log.info("Inserted item is " + itemCapped));
    }
}
