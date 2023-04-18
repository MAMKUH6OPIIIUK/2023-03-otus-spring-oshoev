package ru.otus.spring.homework.oke.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Question {
    private final String text;

    private final QuestionType type;

    private final List<Answer> answers;

    public Question(String text, String type, List<Answer> answers) {
        this.text = text;
        this.type = QuestionType.fromString(type);
        this.answers = answers;
    }

    public Answer getAnswerByText(String answerText) {
        for (Answer answer : this.answers) {
            if (answer.getText().equalsIgnoreCase(answerText)) {
                return answer;
            }
        }
        return null;
    }

    public Answer getAnswerWithMaxScore() {
        return this.answers.stream().max(new Comparator<Answer>() {
            @Override
            public int compare(Answer answer1, Answer answer2) {
                if (answer1.getScore() > answer2.getScore()) {
                    return 1;
                } else if (answer1.getScore() < answer2.getScore()) {
                    return -1;
                }
                return 0;
            }
        }).get();
    }
}
