package ru.itis.service.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.service.auth.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
