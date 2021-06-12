package com.infinibet.dto;

import com.infinibet.model.Outcome;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class OutcomeDto {
    private Long id;
    private String name;
    private Long eventId;
    private BigDecimal coefficient;
    private BigDecimal pooledRealMoney;
    private BigDecimal pooledVirtualMoney;

    public OutcomeDto(Outcome outcome) {
        this.id = outcome.getId();
        this.name = outcome.getName();
        this.eventId = outcome.getEvent().getId();
        this.coefficient = outcome.getCoefficient();
        this.pooledRealMoney = outcome.getPooledRealMoney();
        this.pooledRealMoney = outcome.getPooledVirtualMoney();
    }
}
