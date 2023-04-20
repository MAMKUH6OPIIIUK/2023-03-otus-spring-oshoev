package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.otus.spring.homework.oke.config.ApplicationPropertiesProvider;
import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования студента ")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestingServiceImplTest {
    @Mock
    TestingDao testingDao;

    @Mock
    IOService ioService;

    @Mock
    LocalizeService localizeService;

    TestingServiceImpl testingService;

    private final static String EXPECTED_STUDENT_NAME_PROMPT = "Введите Ваше имя";

    private final static String EXPECTED_STUDENT_SURNAME_PROMPT = "Введите Вашу фамилию";

    private final static String EXPECTED_SELECT_ONE_ANSWER_PROMPT = "Скопируйте и вставьте один из предложенных вариантов";

    private final static String EXPECTED_FREE_ANSWER_PROMPT = "Введите Ваш вариант ответа";


    @BeforeEach
    public void setUp() {
        Map<String, Object> serviceProperties = new HashMap<>();
        String themeName = "Test theme";
        serviceProperties.put(ApplicationPropertiesProvider.TESTING_THEME_PROPERTY, themeName);
        Integer passingScore = 10;
        serviceProperties.put(ApplicationPropertiesProvider.TESTING_PASSING_SCORE_PROPERTY, passingScore);
        ApplicationPropertiesProvider propertiesProvider = new ApplicationPropertiesProvider();
        propertiesProvider.setService(serviceProperties);
        testingService = new TestingServiceImpl(testingDao, propertiesProvider, ioService, localizeService);

        given(localizeService.getMessage(any())).willReturn(new String());
        given(localizeService.getMessage(any(), any())).willReturn(new String());
        given(localizeService.getMessage(TestingServiceImpl.STUDENT_NAME_PROMPT_CODE))
                .willReturn(EXPECTED_STUDENT_NAME_PROMPT);
        given(localizeService.getMessage(TestingServiceImpl.STUDENT_SURNAME_PROMPT_CODE))
                .willReturn(EXPECTED_STUDENT_SURNAME_PROMPT);
        given(localizeService.getMessage(TestingServiceImpl.SELECT_ONE_ANSWER_PROMPT_CODE))
                .willReturn(EXPECTED_SELECT_ONE_ANSWER_PROMPT);
        given(localizeService.getMessage(TestingServiceImpl.FREE_ANSWER_PROMPT_CODE))
                .willReturn(EXPECTED_FREE_ANSWER_PROMPT);
    }

    @DisplayName(" должен через IOService: локализованно спрашивать имя и фамилию студента, задавать все вопросы " +
            "и выводить итоговый балл")
    @Test
    public void shouldAskStudentNameAskAllQuestionsAndReturnScore() {
        String themeName = "Test theme";
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

        testingService.executeStudentTesting();

        verify(ioService, times(1)).readLineWithPrompt(EXPECTED_STUDENT_NAME_PROMPT);
        verify(ioService, times(1)).readLineWithPrompt(EXPECTED_STUDENT_SURNAME_PROMPT);
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
        String correctSelectableAnswer = "Perl";
        String correctFreeAnswer = "Губка Боб";
        Integer expectedScore = testing.getMaxScore();

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.TESTING_PASSED_FORMAT_CODE,
                new Object[]{expectedName, expectedScore})).
                willReturn("Дорогой " + expectedName + "! Поздравляем, тестирование успешно пройдено. Ваш балл: " +
                        expectedScore);

        given(ioService.readLineWithPrompt(any())).willReturn(new String());
        given(ioService.readLineWithPrompt(EXPECTED_STUDENT_NAME_PROMPT)).willReturn(expectedName);
        given(ioService.readLineWithPrompt(EXPECTED_SELECT_ONE_ANSWER_PROMPT))
                .willReturn(correctSelectableAnswer);
        given(ioService.readLineWithPrompt(EXPECTED_FREE_ANSWER_PROMPT))
                .willReturn(correctFreeAnswer);

        testingService.executeStudentTesting();

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
        String correctSelectableAnswer = "Perl";
        String incorrectFreeAnswer = "Черный плащ";
        Integer expectedScore = 10;

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.TESTING_PASSED_FORMAT_CODE,
                new Object[]{expectedName, expectedScore})).
                willReturn("Дорогой " + expectedName + "! Поздравляем, тестирование успешно пройдено. Ваш балл: " +
                        expectedScore);

        given(ioService.readLineWithPrompt(any())).willReturn(new String());
        given(ioService.readLineWithPrompt(EXPECTED_STUDENT_NAME_PROMPT)).willReturn(expectedName);
        given(ioService.readLineWithPrompt(EXPECTED_SELECT_ONE_ANSWER_PROMPT)).willReturn(correctSelectableAnswer);
        given(ioService.readLineWithPrompt(EXPECTED_FREE_ANSWER_PROMPT)).willReturn(incorrectFreeAnswer);

        testingService.executeStudentTesting();

        verify(ioService, times(1)).printLine(contains(expectedName + "! Поздравляем"));
        verify(ioService, times(1)).printLine(contains("Ваш балл: " + expectedScore));
    }

    @DisplayName(" должен не засчитать успешность тестирования, если не набран проходной балл")
    @Test
    public void shouldPerformStudentTestAndNotifyThatTestingFailedIfWrongAnswers() {
        String themeName = "Test theme";
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

        testingService.executeStudentTesting();

        verify(ioService, times(1)).printLine(contains("Sorry, you didn't pass the test"));
        verify(ioService, times(1)).printLine(contains("Your score: " + expectedScore));
    }

    @DisplayName(" должен сообщить, что тестирование по теме не найдено, если вопросов по теме не получено от дао")
    @Test
    public void shouldNotifyThatTestingNotFound() {
        String themeName = "Test theme2";
        Testing testing = generateTestingData(themeName);

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        given(localizeService.getMessage(TestingServiceImpl.TESTING_NOT_FOUND_CODE)).
                willReturn("Sorry. Testing not found");

        testingService.executeStudentTesting();

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
