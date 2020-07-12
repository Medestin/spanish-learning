package com.medestin.spanish.learning.service;

import com.medestin.spanish.learning.model.Word;
import com.medestin.spanish.learning.model.WordType;
import com.medestin.spanish.learning.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

    @Mock
    WordRepository repository;

    @InjectMocks
    WordService service;

    private static final String testWordValue = "testValue1";
    private static final Word testWord = new Word(testWordValue, "testMeaning1", WordType.NOUN);

    @Test
    void save_returnsWord_wordSaved() {
        doReturn(Mono.just(testWord)).when(repository).save(testWord);

        StepVerifier.create(service.save(testWord))
                .consumeNextWith(word -> assertEquals(testWord, word))
                .verifyComplete();
    }

    @Test
    void save_returnsMonoError_exceptionThrown() {
        Mono<Word> mono = Mono.just(testWord)
                .map(word -> {
                    throw new RuntimeException();
                });
        doReturn(mono).when(repository).save(testWord);

        StepVerifier.create(service.save(testWord))
                .consumeErrorWith(exc -> assertTrue(exc instanceof WordServiceException))
                .verify();
    }

    @Test
    void save_returnsEmpty_wordAlreadyExists() {
        doReturn(Mono.empty()).when(repository).save(testWord);

        StepVerifier.create(service.save(testWord))
                .verifyComplete();
    }

    @Test
    void readByValue_returnsWord() {
        doReturn(Mono.just(testWord)).when(repository).readByValue(testWordValue);

        StepVerifier.create(service.readByValue(testWordValue))
                .consumeNextWith(word -> assertEquals(testWord, word))
                .verifyComplete();
    }

    @Test
    void readByValue_returnsMonoError_ExceptionThrown() {
        Mono<Word> mono = Mono.just(testWord)
                .map(word -> {
                    throw new RuntimeException();
                });
        doReturn(mono).when(repository).readByValue(testWordValue);

        StepVerifier.create(service.readByValue(testWordValue))
                .consumeErrorWith(exc -> assertTrue(exc instanceof WordServiceException))
                .verify();
    }

    @Test
    void readAll_returnsFlux() {
        final String testWord2Value = "testValue2";
        Word testWord2 = new Word(testWord2Value, "testMeaning2", WordType.NOUN);
        doReturn(Flux.just(testWord, testWord2)).when(repository).readAll();

        StepVerifier.create(service.readAll())
                .consumeNextWith(word -> assertEquals(testWordValue, word.getValue()))
                .consumeNextWith(word -> assertEquals(testWord2Value, word.getValue()))
                .verifyComplete();
    }


    @Test
    void readAll_returnsFluxError_ExceptionThrown() {
        Flux<Word> flux = Flux.just(testWord)
                .map(word -> {
                    throw new RuntimeException();
                });
        doReturn(flux).when(repository).readAll();

        StepVerifier.create(service.readAll())
                .consumeErrorWith(exc -> assertTrue(exc instanceof WordServiceException))
                .verify();
    }
}