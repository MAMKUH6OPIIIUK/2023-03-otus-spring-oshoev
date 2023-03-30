package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dao.TestingDao;
import ru.otus.spring.homework.oke.domain.Answer;
import ru.otus.spring.homework.oke.domain.Question;
import ru.otus.spring.homework.oke.domain.QuestionType;
import ru.otus.spring.homework.oke.domain.Testing;


public class SimpleTestingService implements TestingService {
    private TestingDao testingDao;

    public void setTestingDao(TestingDao testingDao) {
        this.testingDao = testingDao;
    }

    /**
     * Метод тестирования студента
     * В данной реализации просто печатает в консоль вопросы и варианты ответа к ним (если вопрос сконфигурирован
     * как вопрос с выбором одного из доступных вариантов), определяет наилучший возможный ответ на
     * вопрос и печатает его.
     *
     * @param themeName    имя темы тестирования. Должно иметь фиксированное значение "Java testing from CSV"
     * @param passingScore количество баллов, необходимых для прохождения теста. При проверке результата сверху
     *                     ограничивается максимальным количеством баллов за тест
     * @return true, если студент прошел тестирование. В данной реализации всегда будет это значение
     */
    @Override
    public boolean executeStudentTesting(String themeName, Integer passingScore) {
        Testing testing = this.testingDao.findByThemeName(themeName);
        if (testing == null) {
            System.out.println("Testing not found");
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
        if (totalResult >= passingScore || totalResult >= testing.getMaxScore()) {
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

}
