package com.infinibet.model.transactions;

import com.infinibet.model.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

@Data
@DiscriminatorValue("IN")
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class InTransaction extends Transaction {
    @Column
    private String inAddress;

    @Column
    private String outAddress;

    public InTransaction(Person person, boolean isVirtualMoney, BigDecimal money, TransactionType type, Date date) {
        super(person, isVirtualMoney, money, type, date);
    }
}
