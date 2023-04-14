package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.otus.spring.homework.oke.config.TestingDaoProperties;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;
import ru.otus.spring.homework.oke.service.TranslationService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Дао для работы с csv файлом с тестированиями ")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestingDaoCsvTest {

    @Mock
    private TranslationService translationService;

    private TestingDao testingDao;

    @BeforeEach
    public void setUp() {
        TestingDaoProperties properties = new TestingDaoProperties();
        properties.setCsv("junit_questions.csv");

        testingDao = new TestingDaoCsv(properties, translationService);
    }

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

        given(translationService.getTranslatedString(any())).willReturn("Unexpected");
        given(translationService.getTranslatedString("testing.theme1.Answer1-1")).willReturn(localizedAnswerTextToFirstQuestion1);
        given(translationService.getTranslatedString("testing.theme1.Answer1-2")).willReturn(localizedAnswerTextToFirstQuestion2);
        given(translationService.getTranslatedString("testing.theme1.Question1")).willReturn(localizedQuestion1Text);
        given(translationService.getTranslatedString("testing.theme1.Answer2-1")).willReturn(localizedAnswerTextToSecondQuestion);
        given(translationService.getTranslatedString("testing.theme1.Question2")).willReturn(localizedQuestion2Text);
        given(translationService.getTranslatedString("testing.theme1")).willReturn(localizedTheme);

        List<Testing> foundTestings = testingDao.findAll();
        assertThat(foundTestings).isNotEmpty();
        assertThat(foundTestings).hasSize(1);
        assertThat(foundTestings).contains(expectedFoundTesting);
    }

}
