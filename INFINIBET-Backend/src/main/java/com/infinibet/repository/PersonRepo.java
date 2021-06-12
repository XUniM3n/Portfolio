package com.infinibet.repository;

import com.infinibet.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepo extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);

    boolean existsByUsername(String username);
}
