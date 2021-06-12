package com.infinibet.dto;

import com.infinibet.model.EventSuggestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EventSuggestionDto {
    private Long id;
    private Long personId;
    private String title;
    private String description;
    private Date submitDate;

    public EventSuggestionDto(EventSuggestion eventSuggestion) {
        this.id = eventSuggestion.getId();
        this.personId = eventSuggestion.getPerson().getId();
        this.title = eventSuggestion.getTitle();
        this.description = eventSuggestion.getDescription();
        this.submitDate = eventSuggestion.getSubmitDate();
    }
}
