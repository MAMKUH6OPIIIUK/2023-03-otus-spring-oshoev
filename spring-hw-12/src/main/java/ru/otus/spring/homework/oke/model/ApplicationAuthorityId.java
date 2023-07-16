package ru.otus.spring.homework.oke.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationAuthorityId {
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "authority", nullable = false, length = 50)
    private String authority;
}
