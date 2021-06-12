package com.market.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "money_history")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class MoneyHistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn
    private User sender;

    @OneToOne
    @JoinColumn
    private User receiver;

    @NonNull
    private BigDecimal amount;
}
