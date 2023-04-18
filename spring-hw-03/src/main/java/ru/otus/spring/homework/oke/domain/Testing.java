package ru.otus.spring.homework.oke.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Testing {
    private final String themeName;

    private final List<Question> questions;

    public Question getQuestionByTypeAndText(QuestionType type, String questionText) {
        for (Question question : this.questions) {
            if (question.getType().equals(type) && question.getText().equals(questionText)) {
                return question;
            }
        }
        return null;
    }

    public Integer getMaxScore() {
        int result = 0;
        for (Question question : this.questions) {
            result += question.getAnswerWithMaxScore().getScore();
        }
        return result;
    }
}
