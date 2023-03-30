package ru.otus.spring.homework.oke.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class SimpleTestingServiceTest {
    @Mock
    TestingDao testingDao;

    @InjectMocks
    SimpleTestingService testingService;

    @Before
    public void setup() {
        initMocks(this);
    }

    /**
     * Простой тест для проверки простого сервиса тестирования студентов:)
     *
     * При любом наборе вопросов тестирование студента проходит успешно.
     * В stdout приложения выводится информация о том, что студент набрал максимальное количество баллов
     */
    @Test
    public void test_executeStudentTesting_With_Question_Then_Testing_Passed() {
        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));

        String themeName = "Test theme";

        Answer answer1 = new Answer("C++", 0);
        Answer answer2 = new Answer("Perl", 5);
        Question question1 = new Question("Какой язык программирования является интерпретируемым?",
                QuestionType.SELECT_ONE, Arrays.asList(answer1, answer2));

        Answer answer3 = new Answer("Губка Боб", 10);
        Question question2 = new Question("Кто проживает на дне океана?",
                QuestionType.FREE,Arrays.asList(answer3));
        Testing testing = new Testing(themeName, Arrays.asList(question1, question2));

        when(testingDao.findByThemeName(themeName)).thenReturn(testing);

        boolean result = testingService.executeStudentTesting(themeName, 15);
        Assert.assertTrue(result);

        Assert.assertTrue(myOut.toString().contains("Your score: 15"));
    }
}
