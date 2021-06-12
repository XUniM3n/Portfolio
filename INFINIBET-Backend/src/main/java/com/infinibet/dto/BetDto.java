package com.infinibet.dto;

import com.infinibet.model.Bet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class BetDto {
    private Long id;
    private Long eventId;
    private Long outcomeId;
    private Long personId;
    private BigDecimal money;
    private boolean isVirtualMoney;
    private Date date;

    public BetDto(Bet bet) {
        this.id = bet.getId();
        this.eventId = bet.getEvent().getId();
        this.outcomeId = bet.getOutcome().getId();
        this.personId = bet.getPerson().getId();
        this.money = bet.getMoney();
        this.isVirtualMoney = bet.getIsVirtualMoney();
        this.date = bet.getDate();
    }
}
