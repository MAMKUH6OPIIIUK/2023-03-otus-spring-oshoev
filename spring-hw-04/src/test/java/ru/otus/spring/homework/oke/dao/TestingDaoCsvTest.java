package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.homework.oke.SpringHw04Application;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Дао для работы с csv файлом с тестированиями ")
@SpringBootTest(classes = SpringHw04Application.class)
public class TestingDaoCsvTest {

    @Autowired
    private TestingDao testingDao;

    @DisplayName("должен возвращать список тестирований с 1 тестированием, содержащим все локализованные вопросы из csv")
    @Test
    public void shouldReturnListWithConfiguredTesting() {
        String localizedAnswerTextToFirstQuestion1 = "С++";
        String localizedAnswerTextToFirstQuestion2 = "Perl";
        String localizedQuestion1Text = "Какой язык программирования является интерпретируемым?";
        String localizedAnswerTextToSecondQuestion = "Спанч Боб";
        String localizedQuestion2Text = "Кто проживает на дне океана?";
        String localizedTheme = "Тестовая тема";
        Answer answerToFirstQuestion1 = new Answer(localizedAnswerTextToFirstQuestion1, 0);
        Answer answerToFirstQuestion2 = new Answer(localizedAnswerTextToFirstQuestion2,10);
        Question question1 = new Question(localizedQuestion1Text, QuestionType.SELECT_ONE,
                Arrays.asList(answerToFirstQuestion1, answerToFirstQuestion2));
        Answer answerToSecondQuestion = new Answer(localizedAnswerTextToSecondQuestion, 10);
        Question question2 = new Question(localizedQuestion2Text, QuestionType.FREE,
                Arrays.asList(answerToSecondQuestion));
        Testing expectedFoundTesting = new Testing(localizedTheme, Arrays.asList(question1, question2));

        List<Testing> foundTestings = testingDao.findAll();
        assertThat(foundTestings).isNotEmpty();
        assertThat(foundTestings).hasSize(1);
        assertThat(foundTestings).contains(expectedFoundTesting);
    }
}
