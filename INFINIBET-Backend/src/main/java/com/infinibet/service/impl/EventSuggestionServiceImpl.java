package com.infinibet.service.impl;

import com.infinibet.exception.ResourceNotFoundException;
import com.infinibet.model.EventSuggestion;
import com.infinibet.model.Person;
import com.infinibet.repository.EventSuggestionRepo;
import com.infinibet.service.EventSuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class EventSuggestionServiceImpl implements EventSuggestionService {
    private static final Logger logger = LoggerFactory.getLogger(EventSuggestionServiceImpl.class);
    private final EventSuggestionRepo eventSuggestionRepo;

    public EventSuggestionServiceImpl(EventSuggestionRepo eventSuggestionRepo) {
        this.eventSuggestionRepo = eventSuggestionRepo;
    }

    @Override
    @Transactional
    public boolean add(Person person, String title, String description) {
        EventSuggestion eventSuggestion = new EventSuggestion(person, title, description, new Date());

        eventSuggestionRepo.save(eventSuggestion);

        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        EventSuggestion suggestion = eventSuggestionRepo.findById(id).orElseThrow(() -> {
            logger.error("Tried to delete event suggestion with ID {}", id);
            return new ResourceNotFoundException("Event Suggestion", "id", id);
        });

        eventSuggestionRepo.delete(suggestion);

        return true;
    }
}
