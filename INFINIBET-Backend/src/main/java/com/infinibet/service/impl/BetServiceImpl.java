package com.infinibet.service.impl;

import com.infinibet.exception.BadRequestException;
import com.infinibet.model.Bet;
import com.infinibet.model.Event;
import com.infinibet.model.Outcome;
import com.infinibet.model.Person;
import com.infinibet.model.transactions.BetTransaction;
import com.infinibet.model.transactions.TransactionType;
import com.infinibet.repository.*;
import com.infinibet.service.BetService;
import com.infinibet.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class BetServiceImpl implements BetService {

    private static final Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);
    private final BetRepo betRepo;
    private final TransactionRepo transactionRepo;
    private final EventRepo eventRepo;
    private final OutcomeRepo outcomeRepo;
    private final PersonRepo personRepo;
    private final SettingService settingService;

    public BetServiceImpl(BetRepo betRepo, TransactionRepo transactionRepo, EventRepo eventRepo,
                          OutcomeRepo outcomeRepo, SettingService settingService, PersonRepo personRepo) {
        this.betRepo = betRepo;
        this.transactionRepo = transactionRepo;
        this.eventRepo = eventRepo;
        this.outcomeRepo = outcomeRepo;
        this.settingService = settingService;
        this.personRepo = personRepo;
    }

    @Override
    @Transactional
    public boolean add(Person person, Long eventId, Long outcomeId, boolean isVirtualMoney, BigDecimal money) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> {
            logger.error("Error while adding bet. No event with id {}", eventId);
            return new BadRequestException("No event with such id");
        });
        Outcome outcome = outcomeRepo.findById(outcomeId).orElseThrow(() -> {
            logger.error("Error while adding bet. No outcome with id {}", outcomeId);
            return new BadRequestException("No outcome with such id");
        });

        if (isVirtualMoney) {
            int test = person.getVirtualMoney().compareTo(money);
            if (test < 0) {
                throw new BadRequestException("Not enough virtual money");
            } else {
                person.setVirtualMoney(person.getVirtualMoney().subtract(money));
            }
        } else {
            int test = person.getVirtualMoney().compareTo(money);
            if (test < 0) {
                throw new BadRequestException("Not enough real money");
            } else {
                person.setRealMoney(person.getRealMoney().subtract(money));
            }
        }

        Bet bet = new Bet(event, outcome, person, money, isVirtualMoney, outcome.getCoefficient(), new Date());

        if (isVirtualMoney) {
            BigDecimal newSystemMoneyValue = settingService.getSystemVirtualMoney().add(money);
            settingService.setSystemVirtualMoney(newSystemMoneyValue);
        } else {
            BigDecimal newSystemMoneyValue = settingService.getSystemRealMoney().add(money);
            settingService.setSystemRealMoney(newSystemMoneyValue);
        }

        betRepo.save(bet);
        personRepo.save(person);

        BetTransaction transaction = new BetTransaction(person, isVirtualMoney, money, TransactionType.BET, new Date(),
                bet);
//    transactionRepo.save(transaction);

        return true;
    }
}
