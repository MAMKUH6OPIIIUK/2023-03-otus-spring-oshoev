package ru.otus.spring.homework.oke.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс предоставляет доступ к данным из CSV-ресурса
 * <p>
 * Данные из ресурса читаются при каждом вызове методов данного класса
 */
public class TestingDaoCsv implements TestingDao {
    private final String csvResourceName;

    /**
     * Конструктор класса
     *
     * @param csvResourceName имя ресурса внутри jar
     */
    public TestingDaoCsv(String csvResourceName) {
        this.csvResourceName = csvResourceName;
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
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(this.csvResourceName)) {
            if (inputStream != null) {
                readFromInputStream(inputStream, result);
                return result;
            } else {
                throw new IncorrectCsvFormatException("CSV resource not found");
            }
        } catch (IncorrectCsvFormatException e) {
            throw e;
        } catch (Throwable e) {
            throw new IncorrectCsvFormatException("Unknown error loading CSV resource: "
                    + e.getClass().getName() + e.getMessage());
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
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser).build()) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    this.parseLineAndSave(line, testings);
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
     * @param line     строка из CSV файла
     * @param testings список тестирований, к которому будет добавлен результат парсинга строки
     * @throws IncorrectCsvFormatException если тип вопроса не является допустимым значением, либо балл за ответ
     *                                     не является числом
     */
    private void parseLineAndSave(String[] line, List<Testing> testings) {
        if (line.length != 5) {
            throw new IncorrectCsvFormatException("Q&A CSV resource must contain 5 columns");
        }
        Testing savedTesting = this.getTestingByThemeOrSaveNewOne(testings, line[0]);
        Question existentQuestion = savedTesting.getQuestionByTypeAndText(QuestionType.fromString(line[1]), line[2]);
        if (existentQuestion != null) {
            updateSavedQuestion(existentQuestion, line);
        } else {
            saveNewQuestion(line, savedTesting);
        }
    }

    /**
     * Метод ищет существующее тестирование по теме в списке тестирований и возвращает его.
     * Если тестирования с указанной темой ещё нет в списке, то будет создано новое пустое тестирование
     * и добавлено в него
     *
     * @param testings  список тестирований
     * @param themeName наименование темы тестирования
     * @return существующее тестирование из списка, либо новое тестирование
     */
    private Testing getTestingByThemeOrSaveNewOne(List<Testing> testings, String themeName) {
        for (Testing testing : testings) {
            if (testing.getThemeName().equalsIgnoreCase(themeName)) {
                return testing;
            }
        }
        Testing newTesting = new Testing(themeName, new ArrayList<>());
        testings.add(newTesting);
        return newTesting;
    }

    /**
     * Метод добавляет к тестированию новый вопрос с вариантом ответа на него из CSV-строки
     *
     * @param line    строка из CSV файла
     * @param testing тестирование, к которому будет добавлен новый вопрос
     * @throws IncorrectCsvFormatException См. {@link #parseLineAndSave(String[], List)}
     */
    private void saveNewQuestion(String[] line, Testing testing) {
        try {
            Answer newAnswerForQuestion = new Answer(line[3], Integer.valueOf(line[4]));
            List<Answer> newAnswers = new ArrayList<>();
            newAnswers.add(newAnswerForQuestion);
            testing.getQuestions().add(new Question(line[2], line[1], newAnswers));
        } catch (Throwable e) {
            this.handleFormatException(e);
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
            } catch (Throwable e) {
                this.handleFormatException(e);
            }
        }
    }

    private void handleFormatException(Throwable e) {
        if (e instanceof NumberFormatException) {
            throw new IncorrectCsvFormatException("Column 4 must be integer");
        } else if (e instanceof IllegalArgumentException) {
            throw new IncorrectCsvFormatException("Column 1: " + e.getMessage());
        } else {
            throw new IncorrectCsvFormatException("Unknown error..");
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
    private CSVParser initParser() {
        return new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .withIgnoreLeadingWhiteSpace(true)
                .withEscapeChar('\\')
                .withQuoteChar('"')
                .build();
    }
}
