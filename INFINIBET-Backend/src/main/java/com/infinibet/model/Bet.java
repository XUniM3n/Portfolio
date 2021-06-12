package com.infinibet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne
    private Event event;

    @JoinColumn(name = "outcome_id")
    @ManyToOne
    private Outcome outcome;

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne
    private Person person;

    @Column(precision = 20, scale = 2)
    private BigDecimal money;

    @Column
    private Boolean isVirtualMoney;

    @Column
    private BetStatus status;

    @Column(precision = 5, scale = 2)
    private BigDecimal coefficient;

    @Temporal(TemporalType.DATE)
    private Date date;

    public Bet(Event event, Outcome outcome, Person person, BigDecimal money, boolean isVirtualMoney,
               BigDecimal coefficient, Date date) {
        this.event = event;
        this.outcome = outcome;
        this.person = person;
        this.money = money;
        this.isVirtualMoney = isVirtualMoney;
        this.coefficient = coefficient;
        this.date = date;
        this.status = BetStatus.MADE;
    }
}
