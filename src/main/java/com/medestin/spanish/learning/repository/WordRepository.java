package com.medestin.spanish.learning.repository;

import com.medestin.spanish.learning.model.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class WordRepository {

    @Autowired
    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<Word> save(Word word) {
        return mongoTemplate.save(word);
    }

    public Flux<Word> readAll() {
        return mongoTemplate.findAll(Word.class);
    }

    public Mono<Word> readByValue(String value) {
        Criteria valueCriteria = Criteria.where("value").is(value);
        Criteria conjugationCriteria = Criteria.where("conjugations").elemMatch(Criteria.where("value").is(value));
        Criteria finalCriteria = new Criteria().orOperator(valueCriteria, conjugationCriteria);
        return mongoTemplate.findOne(
                new Query().addCriteria(finalCriteria), Word.class);
    }
}
