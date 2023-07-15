package ru.otus.spring.homework.oke.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genre {
    @Indexed
    private String name;
}
