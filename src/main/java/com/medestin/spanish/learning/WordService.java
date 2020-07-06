package com.medestin.spanish.learning;

import com.medestin.spanish.learning.model.Word;
import com.medestin.spanish.learning.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
public final class WordService {

    private final WordRepository repository;

    public WordService(WordRepository repository) {
        this.repository = repository;
    }

    public Mono<ResponseEntity<Word>> save(Word word) {
        return repository.save(word)
                .map(w -> ResponseEntity.status(201).body(w))
                .switchIfEmpty(alreadyExists(word))
                .onErrorResume(Objects::nonNull,
                        exception -> {
                    log.error("Unknown error occurred while trying to save [" + word + "]", exception);
                    return Mono.just(ResponseEntity.status(500).build());
                        });

    }

    private Mono<? extends ResponseEntity<Word>> alreadyExists(Word word) {
        log.info("[" + word + "] already exists, returning db value");
        return repository.readByValue(word.getValue())
                .map(ResponseEntity::ok);
    }

}
