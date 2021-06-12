package com.infinibet.repository;

import com.infinibet.model.transactions.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAll(Pageable pageable);
}
