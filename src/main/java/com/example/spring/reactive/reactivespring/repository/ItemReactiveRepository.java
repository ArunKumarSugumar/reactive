package com.example.spring.reactive.reactivespring.repository;

import com.example.spring.reactive.reactivespring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author ArunKumar.Sugumar
 */
public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
}
