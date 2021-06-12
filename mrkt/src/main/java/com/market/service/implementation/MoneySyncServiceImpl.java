package com.market.service.implementation;

import com.market.dto.money.GetBalanceDto;
import com.market.dto.money.ReceivedTransactionDto;
import com.market.dto.money.SentTransactionDto;
import com.market.model.MoneyHistoryEntry;
import com.market.model.Transaction;
import com.market.model.User;
import com.market.model.Wallet;
import com.market.model.TransactionState;
import com.market.repository.MoneyHistoryEntryRepository;
import com.market.repository.TransactionRepository;
import com.market.repository.UserRepository;
import com.market.repository.WalletRepository;
import com.market.service.MoneySyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoneySyncServiceImpl implements MoneySyncService {
    private final static String URL = "https://block.io";
    private final static String GET_BALANCE = "/api/v2/get_address_balance/?api_key=%s&addresses=%s";
    private final static String GET_TRANSACTIONS_RECEIVED = "/api/v2/get_transactions/?api_key=%s&type=received";
    private final static String GET_TRANSACTIONS_RECEIVED_BEFORE = "/api/v2/get_transactions/?api_key=%s&type=received&before_tx=%s";
    private final static String GET_TRANSACTIONS_SENT = "/api/v2/get_transactions/?api_key=%s&type=sent";
    private final static String GET_TRANSACTIONS_SENT_BEFORE = "/api/v2/get_transactions/?api_key=%s&type=sent&before_tx=%s";

    private final RestTemplate restTemplate;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final MoneyHistoryEntryRepository moneyHistoryEntryRepository;
    @Value("${blockio.api.key}")
    private String API_KEY;

    @Autowired
    public MoneySyncServiceImpl(RestTemplate restTemplate,
                                TransactionRepository transactionRepository, UserRepository userRepository,
                                WalletRepository walletRepository, MoneyHistoryEntryRepository moneyHistoryEntryRepository) {
        this.restTemplate = restTemplate;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.moneyHistoryEntryRepository = moneyHistoryEntryRepository;
    }

    //    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void syncBalances() {
        List<User> users = userRepository.findAll();
        LinkedList<Wallet> wallets = new LinkedList<>();
        StringBuilder walletsSb = new StringBuilder();
        for (User user : users) {
            Wallet wallet = user.getWallet();
            if (wallet == null || wallet.getAddress() == null)
                continue;
            wallets.addLast(wallet);
            walletsSb.append(wallet.getAddress());
            walletsSb.append(',');
        }
        walletsSb.deleteCharAt(walletsSb.length() - 1);
        String requestUrl = String.format(URL + GET_BALANCE, API_KEY, walletsSb.toString());
        GetBalanceDto dto;
        try {
            dto = restTemplate.getForObject(requestUrl, GetBalanceDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (dto.getData() == null)
            return;
        for (Wallet wallet : wallets) {
            Optional<GetBalanceDto.GetBalanceData.Balance> balance = dto.getData().getBalances().stream()
                    .filter(b -> b.getAddress().equals(wallet.getAddress())).findFirst();
            if (balance.isPresent() && wallet.getBalance().compareTo(new BigDecimal(balance.get().getAvailableBalance())) != 0) {
                System.err.println("Our and Block.io balance of this wallet does do not match" + wallet.getAddress());
                wallet.setBalance(new BigDecimal(balance.get().getAvailableBalance()));
                walletRepository.save(wallet);
            }
        }
    }

    // Starts at boot
    @Override
    public void updateTransactions() {
        String requestUrl = String.format(URL + GET_TRANSACTIONS_RECEIVED, API_KEY);
        ReceivedTransactionDto dtoReceived;
        try {
            dtoReceived = restTemplate.getForObject(requestUrl, ReceivedTransactionDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        requestUrl = String.format(URL + GET_TRANSACTIONS_SENT, API_KEY);
        SentTransactionDto dtoSent;
        try {
            dtoSent = restTemplate.getForObject(requestUrl, SentTransactionDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Date lastSentDate;
        Date lastReceivedDate;
        Transaction lastSentTransaction = transactionRepository.findFirst1ByStateOrderByDateDesc(TransactionState.SENT);
        Transaction lastReceivedTransaction = transactionRepository.findFirst1ByStateOrderByDateDesc(TransactionState.RECEIVED);
        if (lastReceivedTransaction == null) {
            if (lastSentTransaction == null) {
                // If there is no transaction history, all wallets should start with zero balance.
                for (Wallet wallet : walletRepository.findAll().stream().filter(Wallet::isOur)
                        .collect(Collectors.toCollection(ArrayList::new))) {
                    wallet.setBalance(new BigDecimal(0));

                }
                for (User user : userRepository.findAll())
                    user.setBalance(new BigDecimal(0));
            }
        }
        List<Transaction> transactionsWithLastDate = null;
        if (lastSentTransaction != null) {
            lastSentDate = lastSentTransaction.getDate();
            transactionsWithLastDate = transactionRepository.findAllByDate(lastSentDate);
        } else {
            lastSentDate = new Date(0);
        }
        if (lastReceivedTransaction != null) {
            lastReceivedDate = lastReceivedTransaction.getDate();
            transactionsWithLastDate = transactionRepository.findAllByDate(lastReceivedDate);
        } else {
            lastReceivedDate = new Date(0);
        }
        List<Transaction> transactions = new ArrayList<>();

        List<ReceivedTransactionDto.TransactionsBlockioData.TransactionBlockio> transactionsReceived = dtoReceived.getData().getTransactions();
        boolean found = false;
        do {
            for (ReceivedTransactionDto.TransactionsBlockioData.TransactionBlockio transaction : transactionsReceived) {
                Date transactionDate = new Date(transaction.getTime() * 1000);
                if (lastReceivedDate.compareTo(transactionDate) > 0) {
                    found = true;
                    break;
                }

                String fromAddress = transaction.getSenders().get(0);
                Wallet fromWallet = saveWallet(fromAddress);
                Wallet toWallet = saveWallet(transaction.getAmounts().get(0).getRecipient());
                Optional<User> userOptional = userRepository.findUserByWallet(toWallet);
                if(!userOptional.isPresent()) continue;
                User user = userOptional.get();

                BigDecimal amount = new BigDecimal(0);
                for (ReceivedTransactionDto.TransactionsBlockioData.TransactionBlockio.AmountBlockio amountReceived
                        : transaction.getAmounts()) {
                    amount = amount.add(new BigDecimal(amountReceived.getAmount()));
                }

                Transaction newTransaction = Transaction.builder()
                        .walletTo(toWallet)
                        .walletFrom(fromWallet)
                        .amount(amount)
                        .date(transactionDate)
                        .user(userRepository.findUserByWallet(toWallet).get())
                        .txid(transaction.getTxid())
                        .state(TransactionState.RECEIVED)
                        .build();
                transactions.add(newTransaction);

                user.setBalance(user.getBalance().add(amount));
                toWallet.setBalance(toWallet.getBalance().add(amount));
                userRepository.save(user);
                walletRepository.save(toWallet);
            }
            if (!found && transactionsReceived.size() == 25) {
                requestUrl = String.format(URL + GET_TRANSACTIONS_RECEIVED_BEFORE, API_KEY,
                        transactions.get(transactions.size() - 1).getTxid());
                try {
                    dtoReceived = restTemplate.getForObject(requestUrl, ReceivedTransactionDto.class);
                    transactionsReceived = dtoReceived.getData().getTransactions();
                } catch (Exception e) {
                    return;
                }
            } else {
                found = true;
            }
        } while (!found);

        List<SentTransactionDto.TransactionsBlockioData.TransactionBlockio> transactionsSent = dtoSent.getData().getTransactions();
        found = false;
        do {
            for (SentTransactionDto.TransactionsBlockioData.TransactionBlockio transaction : transactionsSent) {
                Date transactionDate = new Date(transaction.getTime() * 1000);
                if (lastSentDate.compareTo(transactionDate) > 0) {
                    found = true;
                    break;
                }

                String fromAddress = transaction.getSenders().get(0);
                Wallet fromWallet = saveWallet(fromAddress);
                Wallet toWallet = saveWallet(transaction.getAmounts().get(0).getRecipient());
                User user = userRepository.findUserByWallet(fromWallet).get();

                BigDecimal amount = new BigDecimal(0);
                for (SentTransactionDto.TransactionsBlockioData.TransactionBlockio.AmountBlockio amountReceived
                        : transaction.getAmounts()) {
                    amount = amount.add(new BigDecimal(amountReceived.getAmount()));
                }

                Transaction newTransaction = Transaction.builder()
                        .walletFrom(fromWallet)
                        .walletTo(toWallet)
                        .amount(amount)
                        .fee(new BigDecimal(transaction.getAmountTotal()).subtract(amount))
                        .date(transactionDate)
                        .user(user)
                        .txid(transaction.getTxid())
                        .state(TransactionState.SENT)
                        .build();
                transactions.add(newTransaction);

                user.setBalance(user.getBalance().subtract(new BigDecimal(transaction.getAmountTotal())));
                fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
                userRepository.save(user);
                walletRepository.save(fromWallet);
            }
            if (!found && transactionsSent.size() == 25) {
                requestUrl = String.format(URL + GET_TRANSACTIONS_SENT_BEFORE, API_KEY,
                        transactions.get(transactions.size() - 1).getTxid());
                try {
                    dtoSent = restTemplate.getForObject(requestUrl, SentTransactionDto.class);
                    transactionsSent = dtoSent.getData().getTransactions();
                } catch (Exception e) {
                    return;
                }
            } else {
                found = true;
            }
        } while (!found);

        transactions.sort(Comparator.comparing(Transaction::getDate));
        if (transactionsWithLastDate != null) {
            for (Transaction transaction : transactions) {
                if (transactionsWithLastDate.stream().noneMatch(t -> t.getTxid().equals(transaction.getTxid())))
                    transactionRepository.save(transaction);
            }
        } else {
            transactionRepository.saveAll(transactions);
        }
    }

    @Override
    public void resyncTransactions() {
        transactionRepository.deleteAll();
        updateTransactions();
        for (MoneyHistoryEntry entry : moneyHistoryEntryRepository.findAll()) {
            User sender = entry.getSender();
            User receiver = entry.getReceiver();
            sender.setBalance(sender.getBalance().subtract(entry.getAmount()));
            receiver.setBalance(receiver.getBalance().add(entry.getAmount()));
        }
    }

    /**
     * Save wallet address to not repeat it in database, but only mention it's id.
     *
     * @param address Bitcoin address
     */
    @Override
    public Wallet saveWallet(String address) {

        Wallet wallet = walletRepository.findWalletByAddress(address);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setAddress(address);
            wallet.setBalance(new BigDecimal(0));
        }
        walletRepository.save(wallet);
        return wallet;
    }
}
