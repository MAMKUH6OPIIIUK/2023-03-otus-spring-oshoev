package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SimpleTestingServiceTest {
    @Mock
    TestingDao testingDao;

    SimpleTestingService testingService;

    @BeforeEach
    public void init() {
        String themeName = "Test theme";
        Integer passingScore = 10;
        testingService = new SimpleTestingService(testingDao, themeName, passingScore);
    }

    /**
     * Простой тест для проверки простого сервиса тестирования студентов:)
     * <p>
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
                QuestionType.FREE, Arrays.asList(answer3));
        Testing testing = new Testing(themeName, Arrays.asList(question1, question2));

        when(testingDao.findAll()).thenReturn(Arrays.asList(testing));

        boolean result = testingService.executeStudentTesting();

        assertTrue(result);
        assertTrue(myOut.toString().contains("Your score: 15"));
    }

    @Test
    public void test_executeStudentTesting_When_Theme_Not_Exists_Then_Not_Passed() {
        final ByteArrayOutputStream myOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(myOut));
        String themeName = "Test theme2";

        Answer answer1 = new Answer("C++", 0);
        Answer answer2 = new Answer("Perl", 5);
        Question question1 = new Question("Какой язык программирования является интерпретируемым?",
                QuestionType.SELECT_ONE, Arrays.asList(answer1, answer2));

        Answer answer3 = new Answer("Губка Боб", 10);
        Question question2 = new Question("Кто проживает на дне океана?",
                QuestionType.FREE, Arrays.asList(answer3));
        Testing testing = new Testing(themeName, Arrays.asList(question1, question2));

        when(testingDao.findAll()).thenReturn(Arrays.asList(testing));

        boolean result = testingService.executeStudentTesting();

        assertFalse(result);
        assertTrue(myOut.toString().contains("Sorry. Testing not found"));
    }
}
