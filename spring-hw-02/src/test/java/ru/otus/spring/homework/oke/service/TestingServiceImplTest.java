package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования студента ")
@ExtendWith(MockitoExtension.class)
public class TestingServiceImplTest {
    @Mock
    TestingDao testingDao;

    @Mock
    IOService ioService;

    TestingServiceImpl testingService;

    @BeforeEach
    public void setUp() {
        String themeName = "Test theme";
        Integer passingScore = 10;
        testingService = new TestingServiceImpl(testingDao, themeName, passingScore, ioService);
    }

    @DisplayName(" должен через IOService: спрашивать имя и фамилию студента, задавать все вопросы " +
            "и выводить итоговый балл")
    @Test
    public void shouldAskStudentNameAskAllQuestionsAndReturnScore() {
        String themeName = "Test theme";
        Testing testing = generateTestingData(themeName);
        List<Question> expectedQuestions = testing.getQuestions();
        Question expectedQuestion1 = expectedQuestions.get(0);
        Question expectedQuestion2 = expectedQuestions.get(1);

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));
        given(ioService.readStringWithPrompt(any())).willReturn(new String());
        given(ioService.readStringWithPrompt(TestingServiceImpl.SELECT_ONE_ANSWER_PROMPT)).willReturn("Perl");

        testingService.executeStudentTesting();

        verify(ioService, times(1)).readStringWithPrompt(
                TestingServiceImpl.STUDENT_NAME_PROMPT);
        verify(ioService, times(1)).readStringWithPrompt(
                TestingServiceImpl.STUDENT_SURNAME_PROMPT);
        verify(ioService, times(expectedQuestions.size())).outputString(contains("Question #"));
        verify(ioService, times(1)).outputString(contains(expectedQuestion1.getQuestionText()));
        verify(ioService, times(1)).outputString(contains(expectedQuestion2.getQuestionText()));
        verify(ioService, times(1)).outputString(matches(".*Your score: [0-9]+.*"));
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
        given(ioService.readStringWithPrompt(any())).willReturn(new String());
        given(ioService.readStringWithPrompt(TestingServiceImpl.STUDENT_NAME_PROMPT)).willReturn(expectedName);
        given(ioService.readStringWithPrompt(TestingServiceImpl.SELECT_ONE_ANSWER_PROMPT))
                .willReturn(correctSelectableAnswer);
        given(ioService.readStringWithPrompt(TestingServiceImpl.FREE_ANSWER_PROMPT))
                .willReturn(correctFreeAnswer);

        boolean testingResult = testingService.executeStudentTesting();

        assertThat(testingResult).isEqualTo(true);
        verify(ioService, times(1)).outputString(contains(expectedName + "! Congratulations"));
        verify(ioService, times(1)).outputString(contains("Your score: " + expectedScore));
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
        given(ioService.readStringWithPrompt(any())).willReturn(new String());
        given(ioService.readStringWithPrompt(TestingServiceImpl.STUDENT_NAME_PROMPT)).willReturn(expectedName);
        given(ioService.readStringWithPrompt(TestingServiceImpl.SELECT_ONE_ANSWER_PROMPT))
                .willReturn(correctSelectableAnswer);
        given(ioService.readStringWithPrompt(TestingServiceImpl.FREE_ANSWER_PROMPT))
                .willReturn(incorrectFreeAnswer);

        boolean testingResult = testingService.executeStudentTesting();

        assertThat(testingResult).isEqualTo(true);
        verify(ioService, times(1)).outputString(contains(expectedName + "! Congratulations"));
        verify(ioService, times(1)).outputString(contains("Your score: " + expectedScore));
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
        given(ioService.readStringWithPrompt(any())).willReturn(new String());
        given(ioService.readStringWithPrompt(TestingServiceImpl.SELECT_ONE_ANSWER_PROMPT))
                .willReturn(incorrectSelectableAnswer);
        given(ioService.readStringWithPrompt(TestingServiceImpl.FREE_ANSWER_PROMPT))
                .willReturn(incorrectFreeAnswer);

        boolean testingResult = testingService.executeStudentTesting();

        assertThat(testingResult).isEqualTo(false);
        verify(ioService, times(1)).outputString(contains("Sorry, you didn't pass the test"));
        verify(ioService, times(1)).outputString(contains("Your score: " + expectedScore));
    }

    @DisplayName(" должен сообщить, что тестирование по теме не найдено, если вопросов по теме не получено от дао")
    @Test
    public void shouldNotifyThatTestingNotFound() {
        String themeName = "Test theme2";
        Testing testing = generateTestingData(themeName);

        given(testingDao.findAll()).willReturn(Arrays.asList(testing));

        boolean testingResult = testingService.executeStudentTesting();

        assertThat(testingResult).isEqualTo(false);
        verify(ioService, times(1)).outputString(contains("Sorry. Testing not found"));
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
