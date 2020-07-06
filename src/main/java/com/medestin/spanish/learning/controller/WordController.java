package com.medestin.spanish.learning.controller;

import com.medestin.spanish.learning.WordService;
import com.medestin.spanish.learning.model.Word;
import com.medestin.spanish.learning.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    WordRepository repository;

    @Autowired
    WordService service;

    @PostMapping
    public Mono<ResponseEntity<Word>> insert(@RequestBody Word word) {
        return service.save(word);
    }

    @GetMapping("/")
    public Flux<Word> getWords() {
        return repository.readAll();
    }

    @GetMapping
    public Mono<Word> getByValue(@RequestParam("value") String value) {
        return repository.readByValue(value);
    }
}
