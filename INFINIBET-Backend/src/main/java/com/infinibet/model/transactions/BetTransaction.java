package com.infinibet.model.transactions;

import com.infinibet.model.Bet;
import com.infinibet.model.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@DiscriminatorValue("BET")
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BetTransaction extends Transaction {
    @JoinColumn(name = "bet_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Bet bet;

    public BetTransaction(Person person, boolean isVirtualMoney, BigDecimal money, TransactionType type, Date date,
                          Bet bet) {
        super(person, isVirtualMoney, money, type, date);
        this.bet = bet;
    }
}
