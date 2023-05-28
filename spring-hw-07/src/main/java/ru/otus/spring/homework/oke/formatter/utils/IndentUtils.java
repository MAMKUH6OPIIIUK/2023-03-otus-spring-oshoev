package ru.otus.spring.homework.oke.formatter.utils;

public class IndentUtils {
    public static String getEntityPrefix(int indent) {
        checkIndent(indent);
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= indent; i++) {
            result.append("\t");
        }
        return result.toString();
    }

    public static String getEntityLinePrefix(int indent) {
        checkIndent(indent);
        StringBuilder result = new StringBuilder();
        result.append(System.lineSeparator());
        result.append(getEntityPrefix(indent + 1));
        return result.toString();
    }

    private static void checkIndent(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException("indent должен быть положительным числом");
        }
    }
}
