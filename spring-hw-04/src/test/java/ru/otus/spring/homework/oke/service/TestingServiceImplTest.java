package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.homework.oke.SpringHw04Application;
import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования студента ")
@SpringBootTest(classes = SpringHw04Application.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestingServiceImplTest {
    @MockBean
    TestingDao testingDao;

    @MockBean
    IOService ioService;

    @MockBean
    LocalizeService localizeService;

    @Autowired
    TestingServiceImpl testingService;

    private final static String EXPECTED_SELECT_ONE_ANSWER_PROMPT = "Скопируйте и вставьте один из предложенных вариантов";

    private final static String EXPECTED_FREE_ANSWER_PROMPT = "Введите Ваш вариант ответа";


    @BeforeEach
    public void setUp() {
        given(localizeService.getMessage(any())).willReturn(new String());
        given(localizeService.getMessage(any(), any())).willReturn(new String());
        given(localizeService.getMessage(TestingServiceImpl.SELECT_ONE_ANSWER_PROMPT_CODE))
                .willReturn(EXPECTED_SELECT_ONE_ANSWER_PROMPT);
        given(localizeService.getMessage(TestingServiceImpl.FREE_ANSWER_PROMPT_CODE))
                .willReturn(EXPECTED_FREE_ANSWER_PROMPT);
    }

    @DisplayName(" должен через IOService: локализованно задавать все вопросы и выводить итоговый балл")
    @Test
    public void shouldAskStudentNameAskAllQuestionsAndReturnScore() {
        String themeName = "Test theme";
        Student student = new Student("", "");
        Testing testing = generateTestingData(themeName);
        List<Question> expectedQuestions = testing.getQuestions();
        Question expectedQuestion1 = expectedQuestions.get(0);
        Question expectedQuestion2 = expectedQuestions.get(1);

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.QUESTION_FORMAT_CODE,
                new Object[]{1, expectedQuestion1.getText()})).
                willReturn("Вопрос №1: " + expectedQuestion1.getText());
        given(localizeService.getMessage(TestingServiceImpl.QUESTION_FORMAT_CODE,
                new Object[]{2, expectedQuestion2.getText()})).
                willReturn("Вопрос №2: " + expectedQuestion2.getText());
        given(localizeService.getMessage(TestingServiceImpl.TESTING_PASSED_FORMAT_CODE,
                new Object[]{"", 10})).willReturn("Your score: 10");

        given(ioService.readLineWithPrompt(any())).willReturn(new String());
        given(ioService.readLineWithPrompt(EXPECTED_SELECT_ONE_ANSWER_PROMPT)).willReturn("Perl");

        testingService.executeStudentTesting(student);

        verify(ioService, times(expectedQuestions.size())).printLine(contains("Вопрос №"));
        verify(ioService, times(1)).printLine(contains(expectedQuestion1.getText()));
        verify(ioService, times(1)).printLine(contains(expectedQuestion2.getText()));
        verify(ioService, times(1)).printLine(matches(".*Your score: [0-9]+.*"));
    }

    @DisplayName(" должен при получении правильных ответов на все вопросы через IOService поздравить студента " +
            "с успешным прохождением, сообщив итоговый максимальный балл")
    @Test
    public void shouldPerformStudentAndCongratulateStudentIfAllAnswerCorrect() {
        String themeName = "Test theme";
        Testing testing = generateTestingData(themeName);
        String expectedName = "Вася";
        Student student = new Student(expectedName, new String());
        String correctSelectableAnswer = "Perl";
        String correctFreeAnswer = "Губка Боб";
        Integer expectedScore = testing.getMaxScore();

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.TESTING_PASSED_FORMAT_CODE,
                new Object[]{expectedName, expectedScore})).
                willReturn("Дорогой " + expectedName + "! Поздравляем, тестирование успешно пройдено. Ваш балл: " +
                        expectedScore);

        given(ioService.readLineWithPrompt(any())).willReturn(new String());
        given(ioService.readLineWithPrompt(EXPECTED_SELECT_ONE_ANSWER_PROMPT))
                .willReturn(correctSelectableAnswer);
        given(ioService.readLineWithPrompt(EXPECTED_FREE_ANSWER_PROMPT))
                .willReturn(correctFreeAnswer);

        testingService.executeStudentTesting(student);

        verify(ioService, times(1)).printLine(contains(expectedName + "! Поздравляем"));
        verify(ioService, times(1)).printLine(contains("Ваш балл: " + expectedScore));
    }

    @DisplayName(" должен при получении правильных ответов в количестве, достаточном для прохождения теста, " +
            "через IOService поздравить студента с успешным прохождением, сообщив итоговый балл")
    @Test
    public void shouldPerformStudentAndCongratulateStudentIfPassedScore() {
        String themeName = "Test theme";
        Testing testing = generateTestingData(themeName);
        String expectedName = "Вася";
        Student student = new Student(expectedName, new String());
        String correctSelectableAnswer = "Perl";
        String incorrectFreeAnswer = "Черный плащ";
        Integer expectedScore = 10;

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.TESTING_PASSED_FORMAT_CODE,
                new Object[]{expectedName, expectedScore})).
                willReturn("Дорогой " + expectedName + "! Поздравляем, тестирование успешно пройдено. Ваш балл: " +
                        expectedScore);

        given(ioService.readLineWithPrompt(any())).willReturn(new String());
        given(ioService.readLineWithPrompt(EXPECTED_SELECT_ONE_ANSWER_PROMPT)).willReturn(correctSelectableAnswer);
        given(ioService.readLineWithPrompt(EXPECTED_FREE_ANSWER_PROMPT)).willReturn(incorrectFreeAnswer);

        testingService.executeStudentTesting(student);

        verify(ioService, times(1)).printLine(contains(expectedName + "! Поздравляем"));
        verify(ioService, times(1)).printLine(contains("Ваш балл: " + expectedScore));
    }

    @DisplayName(" должен не засчитать успешность тестирования, если не набран проходной балл")
    @Test
    public void shouldPerformStudentTestAndNotifyThatTestingFailedIfWrongAnswers() {
        String themeName = "Test theme";
        Student student = new Student("any", "any");
        Testing testing = generateTestingData(themeName);
        String incorrectSelectableAnswer = "C++";
        String incorrectFreeAnswer = "Черный плащ";
        Integer expectedScore = 0;

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.TESTING_NOT_PASSED_FORMAT_CODE,
                new Object[]{expectedScore})).
                willReturn("Sorry, you didn't pass the test. Your score: " +
                        expectedScore);

        given(ioService.readLineWithPrompt(any())).willReturn(new String());
        given(ioService.readLineWithPrompt(EXPECTED_SELECT_ONE_ANSWER_PROMPT)).willReturn(incorrectSelectableAnswer);
        given(ioService.readLineWithPrompt(EXPECTED_FREE_ANSWER_PROMPT)).willReturn(incorrectFreeAnswer);

        testingService.executeStudentTesting(student);

        verify(ioService, times(1)).printLine(contains("Sorry, you didn't pass the test"));
        verify(ioService, times(1)).printLine(contains("Your score: " + expectedScore));
    }

    @DisplayName(" должен сообщить, что тестирование по теме не найдено, если вопросов по теме не получено от дао")
    @Test
    public void shouldNotifyThatTestingNotFound() {
        String themeName = "Test theme2";
        Student student = new Student("any", "any");
        Testing testing = generateTestingData(themeName);

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.TESTING_NOT_FOUND_CODE)).
                willReturn("Sorry. Testing not found");

        testingService.executeStudentTesting(student);

        verify(ioService, times(1)).printLine(contains("Sorry. Testing not found"));
    }

    private static Testing generateTestingData(String themeName) {
        Answer answer1 = new Answer("C++", 0);
        Answer answer2 = new Answer("Perl", 10);
        Question question1 = new Question("Какой язык программирования является интерпретируемым?",
                QuestionType.SELECT_ONE, Arrays.asList(answer1, answer2));
        Answer answer3 = new Answer("Губка Боб", 5);
        Question question2 = new Question("Кто проживает на дне океана?",
                QuestionType.FREE, Arrays.asList(answer3));
        List<Question> questionsToAsk = Arrays.asList(question1, question2);
        Testing testing = new Testing(themeName, questionsToAsk);
        return testing;
    }
}
