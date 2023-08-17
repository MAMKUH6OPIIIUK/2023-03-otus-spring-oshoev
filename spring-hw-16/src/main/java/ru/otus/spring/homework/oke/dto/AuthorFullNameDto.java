package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorFullNameDto {
    private Long id;

    private String fullName;
}
