package ru.otus.spring.homework.oke.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authorities")
public class ApplicationAuthority implements GrantedAuthority {
    @EmbeddedId
    private ApplicationAuthorityId id;

    @Override
    public String getAuthority() {
        return this.id.getAuthority();
    }
}
