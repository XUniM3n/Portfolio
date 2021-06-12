package com.infinibet.model;

import com.infinibet.config.AppConstants;
import com.infinibet.model.transactions.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @Column
    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(precision = 20, scale = 2)
    @NotNull
    private BigDecimal realMoney;

    @Column(precision = 20, scale = 2)
    @NotNull
    private BigDecimal virtualMoney = AppConstants.START_VIRTUAL_BALANCE;

    @Temporal(TemporalType.DATE)
    private Date regDate;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Bet> bets;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<EventSuggestion> eventSuggestion;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
        this.realMoney = new BigDecimal(0);
        this.virtualMoney = new BigDecimal(1000);
        this.regDate = new Date();
        this.bets = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.eventSuggestion = new ArrayList<>();
    }
}
