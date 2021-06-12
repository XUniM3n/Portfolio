package com.infinibet.controller;

import com.infinibet.config.AppConstants;
import com.infinibet.dto.EventDto;
import com.infinibet.exception.ResourceNotFoundException;
import com.infinibet.model.Event;
import com.infinibet.payload.ApiResponse;
import com.infinibet.payload.NewEventRequest;
import com.infinibet.payload.SetEventResultRequest;
import com.infinibet.payload.SuccessResponse;
import com.infinibet.repository.EventRepo;
import com.infinibet.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/events")
@RestController
public class EventController {

    private final EventRepo eventRepo;
    private final EventService eventService;

    public EventController(EventRepo eventRepo, EventService eventService) {
        this.eventRepo = eventRepo;
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable(value = "id") Long id) {
        Event event = eventRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event", "id", id));

        return new EventDto(event);
    }

    @GetMapping("")
    public List<EventDto> getEventsBy(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "size", required = false) Integer size) {
        if (page == null)
            page = AppConstants.DEFAULT_PAGE_NUMBER;
        if (size == null)
            size = AppConstants.DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = eventRepo.findAllByIsEndedIsFalse(pageable);
        List<Event> events = eventsPage.getContent();
        return events.stream().map(EventDto::new).collect(Collectors.toList());
    }

    @PostMapping("/set-result")
    public ApiResponse setEventResult(@RequestBody @Valid SetEventResultRequest request) {
        eventService.setResult(request.getEventId(), request.getOutcomeId());

        return new SuccessResponse("Outcome set");
    }

    @PostMapping("/add")
    public ApiResponse addEvent(@RequestBody @Valid NewEventRequest request) {
        HashMap<String, BigDecimal> outcomes = new HashMap<>();
        for (NewEventRequest.Outcome outcome : request.getOutcomes()) {
            outcomes.put(outcome.getName(), outcome.getCoefficient());
        }

        eventService.add(request.getTitle(), request.getDescription(), outcomes);
        return new SuccessResponse("Event added");
    }
}
