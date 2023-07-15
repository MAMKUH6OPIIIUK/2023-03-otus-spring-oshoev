package ru.otus.spring.homework.oke.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.dto.AuthorRequestDto;

@Component
public class AuthorMapper {
    public Author mapToAuthor(AuthorRequestDto authorRequestDto) {
        Author author = new Author(authorRequestDto.getName(), authorRequestDto.getMiddleName(),
                authorRequestDto.getPatronymic(), authorRequestDto.getSurname());
        return author;
    }

    public AuthorFullNameDto mapToAuthorFullNameDto(Author author) {
        StringBuilder fullName = new StringBuilder();
        fullName.append(author.getName());
        String middleName = author.getMiddleName();
        fullName.append(middleName == null ? "" : " " + middleName);
        String patronymic = author.getPatronymic();
        fullName.append(patronymic == null ? "" : " " + patronymic);
        fullName.append(" ").append(author.getSurname());
        AuthorFullNameDto dto = new AuthorFullNameDto();
        dto.setId(author.getId());
        dto.setFullName(fullName.toString());
        return dto;
    }
}
