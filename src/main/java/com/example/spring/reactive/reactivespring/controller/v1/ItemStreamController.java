package com.example.spring.reactive.reactivespring.controller.v1;

import com.example.spring.reactive.reactivespring.document.ItemCapped;
import com.example.spring.reactive.reactivespring.repository.ItemReactiveCappedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.example.spring.reactive.reactivespring.constants.ItemConstants.ITEM_STREAM_ENDPOINT_V1;

/**
 * @author ArunKumar.Sugumar
 */
@RestController
public class ItemStreamController {

    @Autowired
    ItemReactiveCappedRepository itemReactiveCappedRepository;

    @GetMapping(value = ITEM_STREAM_ENDPOINT_V1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemStream() {
        return itemReactiveCappedRepository.findItemsBy();
    }
}
