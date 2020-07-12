package com.medestin.spanish.learning;

import com.medestin.spanish.learning.model.Word;
import com.medestin.spanish.learning.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
public final class WordService {

    private final WordRepository repository;

    public WordService(WordRepository repository) {
        this.repository = repository;
    }

    public Mono<Word> save(Word word) {
        return repository.save(word)
                .onErrorResume(Objects::nonNull,
                        exception -> {
                            String message = String.format("Unknown error occurred while trying to save [%s]", word);
                            return Mono.error(logAndWrap(message, exception));
                        });
    }

    public Mono<Word> readByValue(String value) {
        return repository.readByValue(value)
                .onErrorResume(Objects::nonNull,
                        exception -> {
                            String message = String.format("Unknown error occurred while trying to look up word by value [%s]", value);
                            return Mono.error(logAndWrap(message, exception));
                        });
    }

    public Flux<Word> readAll() {
        return repository.readAll()
                .onErrorResume(Objects::nonNull,
                        exception -> {
                            String message = "Unknown error occurred while trying to look up all words";
                            return Flux.error(logAndWrap(message, exception));
                        });

    }

    private WordServiceException logAndWrap(String message, Throwable exception) {
        WordServiceException wordException = new WordServiceException(message, exception);
        log.error(message, wordException);
        return wordException;
    }
}
