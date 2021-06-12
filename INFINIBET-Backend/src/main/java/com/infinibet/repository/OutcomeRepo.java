package com.infinibet.repository;

import com.infinibet.model.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutcomeRepo extends JpaRepository<Outcome, Long> {
}
