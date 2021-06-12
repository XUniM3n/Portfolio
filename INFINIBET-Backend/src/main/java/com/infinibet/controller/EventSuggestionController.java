package com.infinibet.controller;

import com.infinibet.config.AppConstants;
import com.infinibet.dto.EventSuggestionDto;
import com.infinibet.exception.ResourceNotFoundException;
import com.infinibet.model.EventSuggestion;
import com.infinibet.payload.ApiResponse;
import com.infinibet.payload.DeleteRequest;
import com.infinibet.payload.EventSuggestionRequest;
import com.infinibet.payload.SuccessResponse;
import com.infinibet.repository.EventSuggestionRepo;
import com.infinibet.service.EventSuggestionService;
import com.infinibet.util.PersonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/event-suggestions")
@RestController
public class EventSuggestionController {
    private final EventSuggestionService eventSuggestionService;
    private final EventSuggestionRepo eventSuggestionRepo;
    private final PersonUtil personUtil;

    public EventSuggestionController(EventSuggestionService eventSuggestionService, PersonUtil personUtil, EventSuggestionRepo eventSuggestionRepo) {
        this.eventSuggestionService = eventSuggestionService;
        this.personUtil = personUtil;
        this.eventSuggestionRepo = eventSuggestionRepo;
    }

    @GetMapping("/{id}")
    public EventSuggestionDto getEventById(@PathVariable(value = "id") Long id) {
        EventSuggestion eventSuggestion = eventSuggestionRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("EventSuggestion", "id", id));

        return new EventSuggestionDto(eventSuggestion);
    }

    @GetMapping("")
    public List<EventSuggestionDto> getEventsSuggestionsBy(@RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "size", required = false) Integer size) {
        if (page == null)
            page = AppConstants.DEFAULT_PAGE_NUMBER;
        if (size == null)
            size = AppConstants.DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(page, size);
        Page<EventSuggestion> eventSuggestionsPage = eventSuggestionRepo.findAll(pageable);
        List<EventSuggestion> suggestions = eventSuggestionsPage.getContent();
        return suggestions.stream().map(EventSuggestionDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ApiResponse submitEvent(@RequestBody @Valid EventSuggestionRequest suggestion) {
        eventSuggestionService.add(personUtil.getCurrentPerson(), suggestion.getTitle(), suggestion.getDescription());
        return new SuccessResponse("Event suggestion submitted");
    }

    @PostMapping("/delete/{id}")
    public ApiResponse getEventById(@RequestBody DeleteRequest request) {
        eventSuggestionService.delete(request.getId());

        return new SuccessResponse("Successfully deleted");
    }
}
