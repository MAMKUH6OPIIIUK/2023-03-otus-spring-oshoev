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
 * Класс предоставляет доступ к данными из CSV-ресурса
 * <p>
 * При создании экземпляра класса содержимое ресурса сразу читается в "кэш", и в при дальнейших обращениях к экземпляру
 * данные возвращаются именно из этого кэша
 */
public class TestingDaoCsv implements TestingDao {
    private final Testing testingCache;

    /**
     * Конструктор класса
     *
     * @param csvResourceName имя ресурса внутри jar
     * @throws IOException
     * @throws IncorrectCsvFormatException если строка в CSV-ресурсе имеет неожиданный набор стоблцов и их значений.
     *                                     См. {@link #parseLineAndSave(String[])}
     */
    public TestingDaoCsv(String csvResourceName) throws IOException, IncorrectCsvFormatException {
        this.testingCache = new Testing("Java testing from CSV", new ArrayList<>());
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(csvResourceName);
        readFromInputStream(inputStream);
    }

    private void readFromInputStream(InputStream inputStream)
            throws IOException, IncorrectCsvFormatException {
        CSVParser parser = initParser();
        try (Reader reader
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser).build()) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    this.parseLineAndSave(line);
                }
            }
        }
    }

    /**
     * Метод разбирает CSV строку и добавляет её к кешированной сущности тестирования.
     * Столбцы строки должны соответствовать следующему формату:
     * 0. Строка с типом вопроса: free (свободный ввод ответа - вариант не должен показываться студенту)
     * select_one (вопрос может иметь несколько вариантов ответа, показываются студенту, он выбирает один)
     * 1. Текст вопроса.
     * 2. Текст с вариантом ответа
     * 3. Целочисленный балл за выбор данного варианта
     *
     * @param line строка из CSV файла
     * @throws IncorrectCsvFormatException если тип вопроса не является допустимым значением, либо балл за ответ
     *                                     не является числом
     */
    private void parseLineAndSave(String[] line) throws IncorrectCsvFormatException {
        if (line.length != 4) {
            throw new IncorrectCsvFormatException("Q&A CSV resource must contain 4 columns");
        }

        Question cachedQuestion = this.testingCache.getQuestionByTypeAndText(QuestionType.fromString(line[0]), line[1]);
        if (cachedQuestion != null) {
            updateCachedQuestion(cachedQuestion, line);
        } else {
            cacheNewQuestion(line);
        }
    }

    private void cacheNewQuestion(String[] line) throws IncorrectCsvFormatException {
        try {
            Answer newAnswerForQuestion = new Answer(line[2], Integer.valueOf(line[3]));
            List<Answer> newAnswers = new ArrayList<>();
            newAnswers.add(newAnswerForQuestion);
            this.testingCache.getQuestions().add(new Question(line[1], line[0], newAnswers));
        } catch (Throwable e) {
            this.handleException(e);
        }
    }

    private void updateCachedQuestion(Question cachedQuestion, String[] line) {
        Answer savedAnswer = cachedQuestion.getAnswerByText(line[2]);
        if (savedAnswer != null) {
            return;
        } else {
            Answer newAnswerForQuestion = new Answer(line[2], Integer.valueOf(line[3]));
            cachedQuestion.getAnswers().add(newAnswerForQuestion);
        }
    }

    private void handleException(Throwable e) throws IncorrectCsvFormatException {
        if (e instanceof NumberFormatException) {
            throw new IncorrectCsvFormatException("Column 3 must be integer");
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


    @Override
    public List<Testing> findAll() {
        List<Testing> result = new ArrayList<>();
        result.add(this.testingCache);
        return result;
    }

    @Override
    public Testing findByThemeName(String themeName) {
        if (themeName != null && themeName.equals(this.testingCache.getThemeName())) {
            return findAll().get(0);
        } else {
            return null;
        }
    }
}
