package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;

import java.util.List;


public class SimpleTestingService implements TestingService {
    private TestingDao testingDao;

    private String testingTheme;

    private Integer passingScore;

    public SimpleTestingService(TestingDao testingDao, String testingTheme, Integer passingScore) {
        this.testingDao = testingDao;
        this.testingTheme = testingTheme;
        this.passingScore = passingScore;
    }

    /**
     * Метод тестирования студента
     * В данной реализации просто печатает в консоль вопросы и варианты ответа к ним (если вопрос сконфигурирован
     * как вопрос с выбором одного из доступных вариантов), определяет наилучший возможный ответ на
     * вопрос и печатает его.
     *
     * @return true, если студент прошел тестирование. В данной реализации всегда будет это значение
     */
    @Override
    public boolean executeStudentTesting() {
        List<Testing> testings = this.testingDao.findAll();
        Testing testing = this.chooseTesting(testings);
        if (testing == null) {
            System.out.println("Sorry. Testing not found");
            return false;
        }

        this.printHeader(testing);

        int totalResult = 0;
        for (int questionNumber = 1; questionNumber <= testing.getQuestions().size(); questionNumber++) {
            Question currentQuestion = testing.getQuestions().get(questionNumber - 1);
            printQuestion(questionNumber, currentQuestion);
            Answer studentAnswer = readStudentAnswer(currentQuestion);
            totalResult += studentAnswer.getScore();
        }
        if (totalResult >= this.passingScore || totalResult >= testing.getMaxScore()) {
            System.out.println("Congratulations, the test has passed successfully. Your score: " + totalResult);
            return true;
        } else {
            System.out.println("Sorry, you didn't pass the test. try later");
            return false;
        }
    }

    private void printHeader(Testing testing) {
        System.out.println("Attention! Testing begins");
        System.out.println("The test contains " + testing.getQuestions().size()
                + " questions.");
        System.out.println("Maximum Points: " + testing.getMaxScore());
    }

    private void printQuestion(int questionNumber, Question question) {
        System.out.println("Question #" + questionNumber + ": " + question.getQuestionText());
        if (question.getQuestionType().equals(QuestionType.SELECT_ONE)) {
            System.out.println("Copy and paste the text of one of the suggested answers:");
            for (Answer answer : question.getAnswers()) {
                System.out.println(answer.getAnswerText());
            }
        } else {
            System.out.println("Enter your answer");
        }
    }

    /**
     * В данной реализации метод просто ищет самый правильный вариант ответа самостоятельно
     *
     * @param question
     */
    private Answer readStudentAnswer(Question question) {
        Answer answer = question.getAnswerWithMaxScore();
        System.out.println("Student entered answer: " + answer.getAnswerText());
        return answer;
    }

    private Testing chooseTesting(List<Testing> testings) {
        for (Testing testing : testings) {
            if (testing.getThemeName().equalsIgnoreCase(this.testingTheme)) {
                return testing;
            }
        }
        return null;
    }

}
