package com.market.repository;

import com.market.model.MoneyHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyHistoryEntryRepository extends JpaRepository<MoneyHistoryEntry, Long> {
}
