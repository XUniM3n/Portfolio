package com.infinibet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String title;

    @Column(length = 3000)
    private String description;

    @OneToMany(mappedBy = "event")
    private List<Outcome> outcomes;

    @JoinColumn(name = "won_outcome")
    @OneToOne
    private Outcome wonOutcome;

    @Column
    private BigDecimal pooledMoney;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Bet> bets;

    @Temporal(TemporalType.DATE)
    private Date createDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column
    private Boolean isEnded;

    public Event(String title, String description, List<Outcome> outcomes, Date createDate) {
        this.title = title;
        this.description = description;
        this.outcomes = outcomes;
        this.createDate = createDate;
        this.isEnded = false;
        bets = new ArrayList<>();
        pooledMoney = new BigDecimal(0);
    }
}
