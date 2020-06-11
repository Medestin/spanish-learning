package com.medestin.spanish.learning.model;

import lombok.Data;

@Data
public class Word {

    private final String value;
    private final String meaning;
    private final WordType type;

}
