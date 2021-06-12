package com.infinibet.repository;

import com.infinibet.model.EventSuggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSuggestionRepo extends JpaRepository<EventSuggestion, Long> {
    Page<EventSuggestion> findAll(Pageable pageable);
}
