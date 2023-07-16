package ru.otus.spring.homework.oke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.homework.oke.model.ApplicationUser;


public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
}
