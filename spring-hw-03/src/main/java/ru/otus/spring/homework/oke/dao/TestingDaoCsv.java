package ru.otus.spring.homework.oke.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.config.TestingDaoPropertiesProvider;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;
import ru.otus.spring.homework.oke.service.LocalizeService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для доступа к локализованным данным из csv конфигурации тестирования
 * <p>
 * Данные из ресурса читаются при каждом вызове методов данного класса
 */
@Repository
public class TestingDaoCsv implements TestingDao {
    private final URL localizedCsvResourceUrl;

    private final LocalizeService localizeService;

    private final Charset resourceEncoding;

    /**
     * Конструктор класса
     *
     * @param testingDaoPropertiesProvider предоставляет доступ к настройкам dao
     * @param localizeService              сервис локализации
     */
    public TestingDaoCsv(TestingDaoPropertiesProvider testingDaoPropertiesProvider,
                         LocalizeService localizeService) {
        this.resourceEncoding = testingDaoPropertiesProvider.getEncoding();
        String csvResourceBasename = testingDaoPropertiesProvider.getResourceBasename();
        this.localizeService = localizeService;
        String resourceFileFormat = "csv";
        this.localizedCsvResourceUrl = localizeService.getResourceUrl(csvResourceBasename, resourceFileFormat);
        if (localizedCsvResourceUrl == null) {
            String errorMessage = this.localizeService.getMessage("error.csv.not-found");
            throw new IncorrectCsvFormatException(errorMessage);
        }
    }

    /**
     * Метод читает CSV конфигурацию вопросов тестирований
     *
     * @return список сконфигурированных тестирований, если CSV-ресурс имеет корректный формат строк
     * и его удалось прочитать, либо исключение IncorrectCsvFormatException
     */
    @Override
    public List<Testing> findAll() {
        List<Testing> result = new ArrayList<>();

        try (InputStream inputStream = localizedCsvResourceUrl.openStream()) {
            readFromInputStream(inputStream, result);
            return result;
        } catch (IOException e) {
            throw new IncorrectCsvFormatException(this.localizeService.
                    getMessage("error.csv.unknown-error"), e);
        }
    }

    /**
     * Метод загружает CSV-ресурс из InputStream и читает его построчно
     *
     * @param inputStream InputStream читаемого CSV-ресурса
     * @param testings    список тестирований с вопросами по темам, который будет наполнен в результате работы метода
     * @throws IOException
     * @throws IncorrectCsvFormatException если строка в CSV-ресурсе имеет неожиданный набор стоблцов и их значений.
     *                                     См. {@link #parseLineAndSave(String[], List)}
     */
    private void readFromInputStream(InputStream inputStream, List<Testing> testings)
            throws IOException {
        CSVParser parser = initParser();
        try (Reader reader
                     = new BufferedReader(new InputStreamReader(inputStream, this.resourceEncoding))) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser).build()) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    parseLineAndSave(line, testings);
                }
            }
        }
    }

    /**
     * Метод разбирает CSV строку и добавляет её к списку тестирований
     * * Столбцы строки должны соответствовать следующему формату:
     * * 0. Строка с наименованием темы тестирования, к которой относится вопрос
     * * 1. Строка с типом вопроса: free (свободный ввод ответа - вариант не должен показываться студенту)
     * * select_one (вопрос может иметь несколько вариантов ответа, показываются студенту, он выбирает один)
     * * 2. Текст вопроса.
     * * 3. Текст с вариантом ответа
     * * 4. Целочисленный балл за выбор данного варианта
     *
     * @param csvLine  строка из CSV файла
     * @param testings список тестирований, к которому будет добавлен результат парсинга строки
     * @throws IncorrectCsvFormatException если тип вопроса не является допустимым значением, либо балл за ответ
     *                                     не является числом
     */
    private void parseLineAndSave(String[] csvLine, List<Testing> testings) {
        if (csvLine.length != 5) {
            throw new IncorrectCsvFormatException(this.localizeService.
                    getMessage("error.csv.wrong-row-length"));
        }
        Testing savedTesting = getTestingByTheme(testings, csvLine[0]);
        if (savedTesting == null) {
            savedTesting = saveNewTesting(testings, csvLine[0]);
        }
        QuestionType rowQuestionType;
        try {
            rowQuestionType = QuestionType.fromString(csvLine[1]);
        } catch (IllegalArgumentException e) {
            throw new IncorrectCsvFormatException(this.localizeService
                    .getMessage("error.csv.wrong-question-type"));
        }
        Question existentQuestion = savedTesting.getQuestionByTypeAndText(rowQuestionType, csvLine[2]);
        if (existentQuestion != null) {
            updateSavedQuestion(existentQuestion, csvLine);
        } else {
            saveNewQuestion(savedTesting, csvLine);
        }
    }

    /**
     * Метод ищет существующее тестирование по теме в списке тестирований и возвращает его.
     *
     * @param testings  список тестирований
     * @param themeName наименование темы тестирования
     * @return существующее тестирование из списка
     */
    private Testing getTestingByTheme(List<Testing> testings, String themeName) {
        for (Testing testing : testings) {
            if (testing.getThemeName().equalsIgnoreCase(themeName)) {
                return testing;
            }
        }
        return null;
    }

    /**
     * Метод создает новое тестирование по теме и добавляет его в список тестирований.
     *
     * @param testings  список тестирований
     * @param themeName наименование темы тестирования
     * @return новое тестирование
     */
    private Testing saveNewTesting(List<Testing> testings, String themeName) {
        List<Question> emptyQuestionList = new ArrayList<>();
        Testing newTesting = new Testing(themeName, emptyQuestionList);
        testings.add(newTesting);
        return newTesting;
    }

    /**
     * Метод добавляет к тестированию новый вопрос с вариантом ответа на него из CSV-строки
     *
     * @param testing тестирование, к которому будет добавлен новый вопрос
     * @param line    строка из CSV файла
     * @throws IncorrectCsvFormatException См. {@link #parseLineAndSave(String[], List)}
     */
    private void saveNewQuestion(Testing testing, String[] line) {
        try {
            Answer newAnswerForQuestion = new Answer(line[3], Integer.valueOf(line[4]));
            List<Answer> newAnswers = new ArrayList<>();
            newAnswers.add(newAnswerForQuestion);
            testing.getQuestions().add(new Question(line[2], line[1], newAnswers));
        } catch (NumberFormatException e) {
            throw new IncorrectCsvFormatException(this.localizeService
                    .getMessage("error.csv.wrong-score"));
        } catch (IllegalArgumentException e) {
            throw new IncorrectCsvFormatException(this.localizeService
                    .getMessage("error.csv.wrong-question-type"));
        }
    }

    /**
     * Метод добавляет к вопросу тестирования новый вариант ответа на него из CSV-строки, если варианта с текстом
     * из строки ещё нет
     *
     * @param savedQuestion вопрос, к списку вариантов ответа на который, будет добавлен новый вариант из CSV строки
     * @param line          строка из CSV файла
     * @throws IncorrectCsvFormatException См. {@link #parseLineAndSave(String[], List)}
     */
    private void updateSavedQuestion(Question savedQuestion, String[] line) {
        Answer savedAnswer = savedQuestion.getAnswerByText(line[3]);
        if (savedAnswer != null) {
            return;
        } else {
            try {
                Answer newAnswerForQuestion = new Answer(line[3], Integer.valueOf(line[4]));
                savedQuestion.getAnswers().add(newAnswerForQuestion);
            } catch (NumberFormatException e) {
                throw new IncorrectCsvFormatException(this.localizeService
                        .getMessage("error.csv.wrong-score"));
            }
        }
    }

    /**
     * Метод инициализирует парсер CSV строк. С такими настройками возможно корректное чтение csv-файлов, в которых:
     * - разделителем столбцов является запятая
     * - значения столбцов могут содержать в себе запятые, но в таком случае значение столбца необходимо заключить
     * в кавычки
     * - значения столбцов могут содержать кавычки, но в таком случае сами кавычки нужно экранировать обратной
     * косой чертой
     *
     * @return парсер CSV строк
     */
    private static CSVParser initParser() {
        return new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .withIgnoreLeadingWhiteSpace(true)
                .withEscapeChar('\\')
                .withQuoteChar('"')
                .build();
    }

}
