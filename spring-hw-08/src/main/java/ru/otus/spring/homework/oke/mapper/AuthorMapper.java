package ru.otus.spring.homework.oke.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.AuthorRequestDto;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.model.Author;

@Component
public class AuthorMapper {
    public Author mapToAuthor(AuthorRequestDto authorRequestDto) {
        Author author = new Author(authorRequestDto.getName(), authorRequestDto.getMiddleName(),
                authorRequestDto.getPatronymic(), authorRequestDto.getSurname());
        return author;
    }

    public AuthorResponseDto mapToAuthorResponseDto(Author author) {
        AuthorResponseDto dto = new AuthorResponseDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setMiddleName(author.getMiddleName());
        dto.setPatronymic(author.getPatronymic());
        dto.setSurname(author.getSurname());
        return dto;
    }
}
