package com.infinibet.repository;

import com.infinibet.model.Bet;
import com.infinibet.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepo extends JpaRepository<Bet, Long> {
    Page<Bet> findAllByPerson(Pageable pageable, Person person);
}
