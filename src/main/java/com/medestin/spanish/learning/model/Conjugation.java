package com.medestin.spanish.learning.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conjugation {

    ConjugationType.PersonType person;
    ConjugationType.GenderType gender;
    ConjugationType.NumeralType numeral;
    String value;
}
