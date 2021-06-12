package com.infinibet.service;

import com.infinibet.model.Person;

public interface EventSuggestionService {
    boolean add(Person person, String title, String description);

    boolean delete(Long id);
}
