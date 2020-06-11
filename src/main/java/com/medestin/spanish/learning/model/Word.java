package com.medestin.spanish.learning.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Word {

    @NonNull
    private final String value;
    @NonNull
    private final String meaning;
    @NonNull
    private WordType type;

    private List<Conjugation> conjugations;
    private String description;
}
