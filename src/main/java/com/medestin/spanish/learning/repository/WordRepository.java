package com.medestin.spanish.learning.repository;

import com.medestin.spanish.learning.model.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WordRepository {

    @Autowired
    private final ReactiveMongoTemplate mongoTemplate;

    /**
     * Saves entity to database or does nothing if exists already.
     * <p>
     * Will check if entity already exists, if so, it will return null.
     * Otherwise, it will persist entity in database and return it.
     *
     * @param word to be persisted
     * @return persisted word, or null if already in database
     */
    public Mono<Word> save(Word word) {
        return Mono.just(word)
                .filterWhen(w -> existsByValue(w.getValue()).map(b -> !b))
                .doOnNext(w -> log.info("Saving entity {}", w))
                .flatMap(mongoTemplate::save);
    }

    public Flux<Word> readAll() {
        return mongoTemplate.findAll(Word.class);
    }

    public Mono<Word> readByValue(String value) {
        return mongoTemplate.findOne(queryByValue(value), Word.class);
    }

    private Mono<Boolean> existsByValue(String value) {
        return mongoTemplate.exists(queryByValue(value), Word.class);
    }

    private Query queryByValue(String value) {
        Criteria valueCriteria = Criteria.where("value").is(value);
        Criteria conjugationCriteria = Criteria.where("conjugations").elemMatch(Criteria.where("value").is(value));
        Criteria finalCriteria = new Criteria().orOperator(valueCriteria, conjugationCriteria);
        return new Query().addCriteria(finalCriteria);
    }
}
