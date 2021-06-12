package ru.itis.services.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.services.users.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
