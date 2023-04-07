package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.homework.oke.Main;
import ru.otus.spring.homework.oke.config.TestConfig;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Дао для работы с csv файлом с тестированиями ")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class, TestConfig.class})
public class TestingDaoCsvTest {

    @Autowired
    private TestingDao testingDao;

    @DisplayName("должен возвращать список тестирований с 1 тестированием, содержащим все вопросы из csv")
    @Test
    public void shouldReturnListWithConfiguredTesting() {
        Answer answerToFirstQuestion1 = new Answer("Answer1-1", 0);
        Answer answerToFirstQuestion2 = new Answer("Answer1-2",10);
        Question question1 = new Question("Question1", QuestionType.SELECT_ONE,
                Arrays.asList(answerToFirstQuestion1, answerToFirstQuestion2));
        Answer answerToSecondQuestion = new Answer("Answer2-1", 10);
        Question question2 = new Question("Question2", QuestionType.FREE,
                Arrays.asList(answerToSecondQuestion));
        Testing mustReturnTesting = new Testing("Theme1", Arrays.asList(question1, question2));

        List<Testing> foundTestings = testingDao.findAll();

        assertThat(foundTestings).isNotEmpty();
        assertThat(foundTestings).hasSize(1);
        assertThat(foundTestings).contains(mustReturnTesting);
    }

}
