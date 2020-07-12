package com.medestin.spanish.learning;

import com.medestin.spanish.learning.model.Word;
import com.medestin.spanish.learning.model.WordType;
import com.medestin.spanish.learning.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

    @Mock
    WordRepository repository;

    @InjectMocks
    WordService service;

    private static final String testWordValue = "testValue1";
    private static final Word testWord = new Word(testWordValue, "testMeaning1", WordType.NOUN);

    @Test
    void save_returns201_wordSaved() {
        doReturn(Mono.just(testWord)).when(repository).save(testWord);
        doReturn(Mono.empty()).when(repository).readByValue(testWordValue);

        StepVerifier.create(service.save(testWord))
                .consumeNextWith(response -> {
                    assertEquals(HttpStatus.CREATED, response.getStatusCode());
                    assertEquals(testWord, response.getBody());
                })
                .verifyComplete();
    }

    @Test
    void save_returns200_wordAlreadyExists() {
        doReturn(Mono.empty()).when(repository).save(testWord);
        doReturn(Mono.just(testWord)).when(repository).readByValue(testWordValue);

        StepVerifier.create(service.save(testWord))
                .consumeNextWith(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(testWord, response.getBody());
                })
                .verifyComplete();
    }

}