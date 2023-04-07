package ru.otus.spring.homework.oke.domain;

import java.util.Objects;

public class Answer {
    private final String answerText;

    private final Integer score;

    public Answer(String answerText, Integer score) {
        this.answerText = answerText;
        this.score = score;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(answerText, answer.answerText) && Objects.equals(score, answer.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerText, score);
    }
}
