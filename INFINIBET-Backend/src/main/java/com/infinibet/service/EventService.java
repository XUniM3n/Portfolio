package com.infinibet.service;

import java.math.BigDecimal;
import java.util.Map;

public interface EventService {
    boolean add(String title, String description, Map<String, BigDecimal> outcomes);

    boolean setResult(Long eventId, Long outcomeId);
}
