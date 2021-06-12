package com.infinibet.service;

import com.infinibet.model.Person;

import java.math.BigDecimal;

public interface BetService {
    boolean add(Person person, Long eventId, Long outcomeId, boolean isVirtualMoney, BigDecimal money);
}
