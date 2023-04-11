package ru.otus.spring.homework.oke.domain;

import java.util.Arrays;

/**
 * Перечисление допустимых типов вопросов
 */
public enum QuestionType {
    // вопрос со свободным вводом ответа (варианты не должны показываться студенту)
    FREE("free"),

    // вопрос с выбором ответа из нескольких вариантов
    SELECT_ONE("select_one");

    private final String type;

    QuestionType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static QuestionType fromString(String type) {
        for (QuestionType questionType : QuestionType.values()) {
            if (questionType.type.equalsIgnoreCase(type)) {
                return questionType;
            }
        }
        throw new IllegalArgumentException("Unsupported question type: \"" + type +
                "\" Valid values: " + Arrays.toString(QuestionType.values()));
    }

    public String toString() {
        return this.type;
    }
}
