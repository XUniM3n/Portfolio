package com.market.service;

import com.market.model.Wallet;

public interface MoneySyncService {
    void updateTransactions();

    void resyncTransactions();

    void syncBalances();

    Wallet saveWallet(String address);
}
