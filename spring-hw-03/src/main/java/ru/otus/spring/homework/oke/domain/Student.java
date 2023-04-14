package ru.otus.spring.homework.oke.domain;

import java.util.Objects;

public class Student {
    private final String name;

    private final String surname;

    public Student(String name, String surname) {
        this.name = name;
        this.surname = name;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(name, student.name) && Objects.equals(surname, student.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }
}
