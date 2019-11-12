package com.example.spring.reactive.reactivespring.handler;

import com.example.spring.reactive.reactivespring.document.Item;
import com.example.spring.reactive.reactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.example.spring.reactive.reactivespring.constants.ItemConstants.ITEM_FUNCTIONAL_ENDPOINT_V1;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * @author ArunKumar.Sugumar
 */
@Component
public class ItemHandler {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class);
    }


    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {

        return itemReactiveRepository.findById(serverRequest.pathVariable("id"))
                .flatMap(item ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(item)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Item.class)
                .flatMap(item -> ServerResponse.created(URI.create(ITEM_FUNCTIONAL_ENDPOINT_V1 + item.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(itemReactiveRepository.save(item), Item.class));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.deleteById(serverRequest.pathVariable("id")), Item.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {

        Mono<Item> updatedItem = serverRequest.bodyToMono(Item.class)
                .flatMap(item -> itemReactiveRepository.findById(serverRequest.pathVariable("id"))
                        .flatMap(currentItem -> {
                            currentItem.setDescription(item.getDescription());
                            currentItem.setPrice(item.getPrice());
                            return itemReactiveRepository.save(currentItem);
                        }));

        return updatedItem.flatMap(item -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(item))
                .switchIfEmpty(ServerResponse.notFound().build()));
    }
}
