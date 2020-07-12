package com.medestin.spanish.learning.controller;

import com.medestin.spanish.learning.service.WordService;
import com.medestin.spanish.learning.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    WordService service;

    @PostMapping
    public Mono<Word> insert(@RequestBody Word word) {
        return service.save(word);
    }

    @GetMapping("/")
    public Flux<Word> getWords() {
        return service.readAll();
    }

    @GetMapping
    public Mono<Word> getByValue(@RequestParam("value") String value) {
        return service.readByValue(value);
    }
}
