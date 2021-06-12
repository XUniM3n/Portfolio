package com.infinibet.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EventSuggestionRequest {
    @NotNull
    private String title;
    private String description;
}
