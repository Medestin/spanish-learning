package com.medestin.spanish.learning.repository;

import com.medestin.spanish.learning.model.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class WordRepository {

    @Autowired
    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<Word> save(Word word) {
        return mongoTemplate.save(word);
    }

    public Mono<Word> readbyValue(String value) {
        return mongoTemplate.findOne(
                new Query().addCriteria(Criteria.where("value").is(value)), Word.class);
    }
}
