package ru.otus.spring.homework.oke.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.config.TestingServicePropertiesProvider;
import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.Testing;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Student;

import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {
    public static final String FREE_ANSWER_PROMPT_CODE = "testing.service.prompt.free-answer";

    public static final String SELECT_ONE_ANSWER_PROMPT_CODE = "testing.service.prompt.select-one";

    public static final String TESTING_NOT_FOUND_CODE = "testing.service.message.not-found";

    public static final String TESTING_BEGIN_MESSAGE_CODE = "testing.service.message.begin";

    public static final String QUESTIONS_COUNT_MESSAGE_CODE = "testing.service.message.questions-count";

    public static final String MAX_SCORE_MESSAGE_CODE = "testing.service.message.max-score";

    public static final String QUESTION_FORMAT_CODE = "testing.service.message.question-format";

    public static final String SELECT_ANSWERS_HEADER_CODE = "testing.service.message.available-answers-header";

    public static final String TESTING_PASSED_FORMAT_CODE = "testing.service.message.passed";

    public static final String TESTING_NOT_PASSED_FORMAT_CODE = "testing.service.message.not-passed";

    private final TestingDao testingDao;

    private final String testingTheme;

    private final Integer passingScore;

    private final IOService ioService;

    private final LocalizeService localizeService;

    public TestingServiceImpl(TestingDao testingDao,
                              TestingServicePropertiesProvider testingServicePropertiesProvider,
                              IOService ioService,
                              LocalizeService localizeService) {
        this.testingDao = testingDao;
        this.testingTheme = testingServicePropertiesProvider.getTheme();
        this.passingScore = testingServicePropertiesProvider.getPassingScore();
        this.ioService = ioService;
        this.localizeService = localizeService;
    }

    /**
     * Метод тестирования студента
     * Запрашивает у дао объекты тестирования, выбирает из списка тестирований тестирование по сконфигурированной теме.
     * Далее по списку вопросов из тестирования:
     * - печатает вопрос и варианты ответов к нему (если вопрос сконфигурирован как вопрос с выбором ответа)
     * - запрашивает у студента ответ на вопрос
     * - прибавляет балл за ответ к суммарному баллу
     * - вычисляет факт успешности или неуспешности тестирования
     */
    @Override
    public void executeStudentTesting(Student student) {
        List<Testing> testings = this.testingDao.findAll();
        Testing testing = this.chooseTesting(testings);
        if (testing == null) {
            String errorMessage = this.localizeService.getMessage(TESTING_NOT_FOUND_CODE);
            this.ioService.printLine(errorMessage);
            return;
        }
        this.printHeader(testing);
        int totalResult = 0;
        for (int questionNumber = 1; questionNumber <= testing.getQuestions().size(); questionNumber++) {
            Question currentQuestion = testing.getQuestions().get(questionNumber - 1);
            printQuestion(questionNumber, currentQuestion);
            Answer studentAnswer = readStudentAnswer(currentQuestion);
            totalResult += studentAnswer.getScore();
        }
        this.calculateTestingResult(testing, student, totalResult);
    }

    private void printHeader(Testing testing) {
        String tempMessage = this.localizeService.getMessage(TESTING_BEGIN_MESSAGE_CODE);
        this.ioService.printLine(tempMessage);
        tempMessage = this.localizeService.getMessage(QUESTIONS_COUNT_MESSAGE_CODE,
                new Integer[]{testing.getQuestions().size()});
        this.ioService.printLine(tempMessage);
        tempMessage = this.localizeService.getMessage(MAX_SCORE_MESSAGE_CODE,
                new Integer[]{testing.getMaxScore()});
        this.ioService.printLine(tempMessage);
    }

    private void printQuestion(int questionNumber, Question question) {
        String formattedQuestion = this.localizeService.getMessage(QUESTION_FORMAT_CODE,
                new Object[]{questionNumber, question.getText()});
        this.ioService.printLine(formattedQuestion);
        if (question.getType().equals(QuestionType.SELECT_ONE)) {
            String selectableAnswersHeader = this.localizeService.getMessage(SELECT_ANSWERS_HEADER_CODE);
            this.ioService.printLine(selectableAnswersHeader);
            for (Answer answer : question.getAnswers()) {
                this.ioService.printLine(answer.getText());
            }
        }
    }

    /**
     * Читает ввод студента и преобразует его к ответу на вопрос.
     * Если вопрос с выбором из нескольких вариантов, студент обязан выбрать один из них
     * (будет предложено повторить ввод при вводе несуществующего варианта)
     * Если вопрос со свободным вводом и ответ неправильный, то ответ будет принят с нулевым баллом за него
     *
     * @param question вопрос, который был задан студенту
     * @return ответ на вопрос
     */
    private Answer readStudentAnswer(Question question) {
        if (question.getType().equals(QuestionType.SELECT_ONE)) {
            return readSelectingAnswer(question);
        } else {
            return readFreeAnswer(question);
        }
    }

    private Answer readSelectingAnswer(Question question) {
        String selectAnswerPrompt = this.localizeService.getMessage(SELECT_ONE_ANSWER_PROMPT_CODE);
        Answer studentAnswer = null;
        while (studentAnswer == null) {
            String studentAnswerText = this.ioService.readLineWithPrompt(selectAnswerPrompt);
            studentAnswer = question.getAnswerByText(studentAnswerText);
        }
        return studentAnswer;
    }

    private Answer readFreeAnswer(Question question) {
        String freeAnswerPrompt = this.localizeService.getMessage(FREE_ANSWER_PROMPT_CODE);
        String studentAnswerText = this.ioService.readLineWithPrompt(freeAnswerPrompt);
        Answer foundAnswer = question.getAnswerByText(studentAnswerText);
        if (foundAnswer != null) {
            return foundAnswer;
        }
        return new Answer(studentAnswerText, 0);
    }

    private Testing chooseTesting(List<Testing> testings) {
        for (Testing testing : testings) {
            if (testing.getThemeName().equalsIgnoreCase(this.testingTheme)) {
                return testing;
            }
        }
        return null;
    }

    private void calculateTestingResult(Testing testing, Student student, int studentScore) {
        if (studentScore >= this.passingScore || studentScore >= testing.getMaxScore()) {
            String passedMessageFormatted = this.localizeService.getMessage(TESTING_PASSED_FORMAT_CODE,
                    new Object[]{student.getName(), studentScore});
            this.ioService.printLine(passedMessageFormatted);
        } else {
            String notPassedMessageFormatted = this.localizeService
                    .getMessage(TESTING_NOT_PASSED_FORMAT_CODE, new Integer[]{studentScore});
            this.ioService.printLine(notPassedMessageFormatted);
        }
    }

}
