package com.medestin.spanish.learning.repository;

import com.medestin.spanish.learning.model.Word;
import com.medestin.spanish.learning.model.WordType;
import com.mongodb.BasicDBList;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordRepositoryTest {

    @Mock
    ReactiveMongoTemplate mongoTemplate;
    @InjectMocks
    WordRepository repository;

    private static final String testWordValue = "testValue1";
    private static final Word testWord = new Word(testWordValue, "testMeaning1", WordType.NOUN);

    @Test
    void readAll_callsReadAll() {
        final String testWord2Value = "testValue2";
        Word testWord2 = new Word(testWord2Value, "testMeaning2", WordType.NOUN);

        doReturn(Flux.just(testWord, testWord2)).when(mongoTemplate).findAll(Word.class);

        Flux<Word> result = repository.readAll();
        StepVerifier.create(result)
                .consumeNextWith(word -> assertEquals(testWordValue, word.getValue()))
                .consumeNextWith(word -> assertEquals(testWord2Value, word.getValue()))
                .verifyComplete();
    }

    @Test
    void readByValue_callsFindOneWithProperArgumentInQuery() {
        doReturn(Mono.just(testWord)).when(mongoTemplate).findOne(any(Query.class), any());

        ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);

        Mono<Word> result = repository.readByValue(testWordValue);
        StepVerifier.create(result)
                .consumeNextWith(word -> assertEquals(testWordValue, word.getValue()))
                .verifyComplete();
        Mockito.verify(mongoTemplate, Mockito.times(1)).findOne(captor.capture(), any());

        BasicDBList list = (BasicDBList) captor.getValue().getQueryObject().get("$or");
        String actual = ((Document) list.get(0)).get("value").toString();
        assertEquals(testWordValue, actual);
    }

    @Test
    void save_persistsRecord_ifRecordDoesNotExistInDatabase() {
        doReturn(Mono.just(Boolean.FALSE)).when(mongoTemplate).exists(any(Query.class), any(Class.class));
        doReturn(Mono.just(testWord)).when(mongoTemplate).save(testWord);

        StepVerifier.create(repository.save(testWord))
                .consumeNextWith(word -> assertEquals(testWordValue, word.getValue()))
                .verifyComplete();


        Mockito.verify(mongoTemplate, times(1)).save(testWord);
    }

    @Test
    void save_doesNotPersist_ifRecordAlreadyExists() {
        doReturn(Mono.just(Boolean.TRUE)).when(mongoTemplate).exists(any(Query.class), any(Class.class));

        StepVerifier.create(repository.save(testWord))
                .verifyComplete();

        Mockito.verify(mongoTemplate, never()).save(any(Word.class));
    }

}