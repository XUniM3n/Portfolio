package com.infinibet.model.transactions;

import com.infinibet.model.Person;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Entity
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "person_id")
    @ManyToOne
    private Person person;

    @Column
    private Boolean isVirtualMoney;

    @Column(precision = 20, scale = 2)
    private BigDecimal money;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Temporal(TemporalType.DATE)
    private Date date;

    public Transaction(Person person, boolean isVirtualMoney, BigDecimal money, TransactionType type, Date date) {
        this.person = person;
        this.isVirtualMoney = isVirtualMoney;
        this.money = money;
        this.type = type;
        this.date = date;
    }
}