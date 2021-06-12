package com.infinibet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class Outcome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;

    @Column(precision = 5, scale = 2)
    private BigDecimal coefficient;

    @Column(precision = 20, scale = 2)
    private BigDecimal pooledRealMoney;

    @Column(precision = 20, scale = 2)
    private BigDecimal pooledVirtualMoney;

    public Outcome(String name, Event event, BigDecimal coefficient) {
        this.name = name;
        this.event = event;
        this.coefficient = coefficient;
        this.pooledRealMoney = new BigDecimal(0);
        this.pooledVirtualMoney = new BigDecimal(0);
    }
}
