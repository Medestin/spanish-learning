package com.medestin.spanish.learning.controller;

import com.medestin.spanish.learning.model.Word;
import com.medestin.spanish.learning.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    WordRepository repository;

    @PostMapping
    public Mono<Word> insert(@RequestBody Word word) {
        return repository.save(word);
    }

    @GetMapping
    public Mono<Word> getByValue(@RequestParam("value") String value) {
        return repository.readbyValue(value);
    }
}
