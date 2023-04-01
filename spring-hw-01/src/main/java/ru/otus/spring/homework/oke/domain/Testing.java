package ru.otus.spring.homework.oke.domain;

import java.util.List;

public class Testing {
    private final String themeName;

    private final List<Question> questions;

    public Testing(String themeName, List<Question> questions) {
        this.themeName = themeName;
        this.questions = questions;
    }

    public String getThemeName() {
        return this.themeName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getQuestionByTypeAndText(QuestionType type, String questionText) {
        for (Question question : this.questions) {
            if (question.getQuestionType().equals(type) && question.getQuestionText().equals(questionText)) {
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
