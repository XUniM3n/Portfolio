package com.infinibet.repository;

import com.infinibet.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByIsEndedIsFalse(Pageable pageable);
}
