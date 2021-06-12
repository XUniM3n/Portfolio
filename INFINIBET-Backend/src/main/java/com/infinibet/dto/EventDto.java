package com.infinibet.dto;

import com.infinibet.model.Event;
import com.infinibet.model.Outcome;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private List<OutcomeDto> outcomes;
    private Outcome wonOutcome;
    private BigDecimal pooledMoney;
    private Date createDate;
    private Date endDate;

    public EventDto(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.outcomes = new ArrayList<>();
        this.wonOutcome = event.getWonOutcome();
        this.pooledMoney = event.getPooledMoney();
        this.createDate = event.getCreateDate();
        this.endDate = event.getEndDate();

        for (Outcome outcome : event.getOutcomes()) {
            this.outcomes.add(new OutcomeDto(outcome));
        }
    }
}
