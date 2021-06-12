package com.market.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    private String txid;

    @OneToOne
    @JoinColumn
    private User user;

    @Column
    @NotNull
    private BigDecimal amount;

    private BigDecimal fee;

    @OneToOne
    @JoinColumn(name = "from_wallet")
    private Wallet walletFrom;

    @OneToOne
    @JoinColumn(name = "to_wallet")
    private Wallet walletTo;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date date;

    @Enumerated(EnumType.ORDINAL)
    private TransactionState state;
}
