package ru.otus.spring.homework.oke.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Answer {
    private final String text;

    private final Integer score;
}
