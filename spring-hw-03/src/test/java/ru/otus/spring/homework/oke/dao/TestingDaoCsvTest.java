package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;
import ru.otus.spring.homework.oke.config.ApplicationPropertiesProvider;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;
import ru.otus.spring.homework.oke.service.LocalizeService;
import ru.otus.spring.homework.oke.service.LocalizeServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Дао для работы с csv файлом с тестированиями ")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestingDaoCsvTest {
    @Mock
    private MessageSource messageSource;

    private TestingDao testingDao;

    @BeforeEach
    public void setUp() {
        ApplicationPropertiesProvider propertiesProvider = new ApplicationPropertiesProvider();
        Map<String, Object> daoProperties = new HashMap<>();
        daoProperties.put(ApplicationPropertiesProvider.DAO_RESOURCE_PROPERTY, "junit_questions");
        daoProperties.put(ApplicationPropertiesProvider.DAO_ENCODING_PROPERTY, "UTF-8");
        propertiesProvider.setDao(daoProperties);
        propertiesProvider.setLocale(new Locale("ru_RU"));
        LocalizeService localizeService = new LocalizeServiceImpl(messageSource, propertiesProvider);
        testingDao = new TestingDaoCsv(propertiesProvider, localizeService);
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

        List<Testing> foundTestings = testingDao.findAll();
        assertThat(foundTestings).isNotEmpty();
        assertThat(foundTestings).hasSize(1);
        assertThat(foundTestings).contains(expectedFoundTesting);
    }

}
