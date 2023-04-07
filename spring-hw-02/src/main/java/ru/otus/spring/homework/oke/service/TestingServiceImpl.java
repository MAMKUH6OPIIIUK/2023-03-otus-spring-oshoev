package ru.otus.spring.homework.oke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.Testing;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Student;

import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {
    public static final String FREE_ANSWER_PROMPT = "Enter your answer";

    public static final String SELECT_ONE_ANSWER_PROMPT = "Copy and paste the text of one of the suggested answers";

    public static final String STUDENT_NAME_PROMPT = "Enter your name";

    public static final String STUDENT_SURNAME_PROMPT = "Enter your surname";

    private TestingDao testingDao;

    private String testingTheme;

    private Integer passingScore;

    private IOService ioService;

    @Autowired
    public TestingServiceImpl(TestingDao testingDao,
                              @Value("${testing.service.theme}") String testingTheme,
                              @Value("${testing.service.passingScore}") Integer passingScore,
                              IOService ioService) {
        this.testingDao = testingDao;
        this.testingTheme = testingTheme;
        this.passingScore = passingScore;
        this.ioService = ioService;
    }

    /**
     * Метод тестирования студента
     * В данной реализации просто печатает в консоль вопросы и варианты ответа к ним (если вопрос сконфигурирован
     * как вопрос с выбором одного из доступных вариантов), определяет наилучший возможный ответ на
     * вопрос и печатает его.
     *
     * @return true, если студент прошел тестирование
     */
    @Override
    public boolean executeStudentTesting() {
        List<Testing> testings = this.testingDao.findAll();
        Testing testing = this.chooseTesting(testings);
        if (testing == null) {
            this.ioService.outputString("Sorry. Testing not found");
            return false;
        }
        this.printHeader(testing);
        Student student = this.askStudentFullName();
        int totalResult = 0;
        for (int questionNumber = 1; questionNumber <= testing.getQuestions().size(); questionNumber++) {
            Question currentQuestion = testing.getQuestions().get(questionNumber - 1);
            printQuestion(questionNumber, currentQuestion);
            Answer studentAnswer = readStudentAnswer(currentQuestion);
            totalResult += studentAnswer.getScore();
        }
        return this.calculateTestingResult(testing, student, totalResult);
    }

    private void printHeader(Testing testing) {
        this.ioService.outputString("Attention! Testing begins");
        this.ioService.outputString("The test contains " + testing.getQuestions().size()
                + " questions.");
        this.ioService.outputString("Maximum Points: " + testing.getMaxScore());
    }

    private Student askStudentFullName() {
        String name = this.ioService.readStringWithPrompt(STUDENT_NAME_PROMPT);
        String surname = this.ioService.readStringWithPrompt(STUDENT_SURNAME_PROMPT);
        return new Student(name,surname);
    }

    private void printQuestion(int questionNumber, Question question) {
        this.ioService.outputString("Question #" + questionNumber + ": " + question.getQuestionText());
        if (question.getQuestionType().equals(QuestionType.SELECT_ONE)) {
            this.ioService.outputString("Suggested answers:");
            for (Answer answer : question.getAnswers()) {
                this.ioService.outputString(answer.getAnswerText());
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
        if (question.getQuestionType().equals(QuestionType.SELECT_ONE)) {
            return readSelectingAnswer(question);
        } else {
            return readFreeAnswer(question);
        }
    }

    private Answer readSelectingAnswer(Question question) {
        Answer studentAnswer = null;
        while (studentAnswer == null) {
            String studentAnswerText = this.ioService.readStringWithPrompt(SELECT_ONE_ANSWER_PROMPT);
            studentAnswer = question.getAnswerByText(studentAnswerText);
        }
        return studentAnswer;
    }

    private Answer readFreeAnswer(Question question) {
        String studentAnswerText = this.ioService.readStringWithPrompt(FREE_ANSWER_PROMPT);
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

    private boolean calculateTestingResult(Testing testing, Student student, int studentScore) {
        if (studentScore >= this.passingScore || studentScore >= testing.getMaxScore()) {
            this.ioService.outputString("Dear " + student.getName() + "! Congratulations, the test has passed " +
                    "successfully. Your score: " + studentScore);
            return true;
        } else {
            this.ioService.outputString("Sorry, you didn't pass the test. Your score: " + studentScore
                    + ". Try later");
            return false;
        }
    }

}
