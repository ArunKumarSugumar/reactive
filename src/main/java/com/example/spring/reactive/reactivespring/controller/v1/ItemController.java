package com.example.spring.reactive.reactivespring.controller.v1;

import com.example.spring.reactive.reactivespring.document.Item;
import com.example.spring.reactive.reactivespring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.example.spring.reactive.reactivespring.constants.ItemConstants.ITEM_ENDPOINT_V1;

/**
 * @author ArunKumar.Sugumar
 */

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_ENDPOINT_V1)
    public Flux<Item> getAllItems() {

        return itemReactiveRepository.findAll();
    }

}
