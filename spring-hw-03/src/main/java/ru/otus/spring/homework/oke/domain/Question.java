package ru.otus.spring.homework.oke.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Question {
    private final String questionText;

    private final QuestionType questionType;

    private final List<Answer> answers;

    public Question(String questionText, QuestionType questionType, List<Answer> answers) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.answers = answers;
    }

    public Question(String questionText, String questionType, List<Answer> answers) {
        this.questionText = questionText;
        this.questionType = QuestionType.fromString(questionType);
        this.answers = answers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public Answer getAnswerByText(String answerText) {
        for (Answer answer : this.answers) {
            if (answer.getAnswerText().equalsIgnoreCase(answerText)) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return Objects.equals(questionText, question.questionText)
                && questionType == question.questionType
                && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionText, questionType, answers);
    }
}
