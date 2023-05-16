package ru.otus.spring.homework.oke.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "surname", nullable = false)
    private String surname;

    public Author(String name, String middleName, String patronymic, String surname) {
        this.name = name;
        this.middleName = middleName;
        this.patronymic = patronymic;
        this.surname = surname;
    }
}
