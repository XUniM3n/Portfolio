package com.infinibet.service.impl;

import com.infinibet.exception.ResourceNotFoundException;
import com.infinibet.model.*;
import com.infinibet.model.transactions.TransactionType;
import com.infinibet.model.transactions.WinTransaction;
import com.infinibet.repository.*;
import com.infinibet.service.EventService;
import com.infinibet.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final PersonRepo personRepo;
    private final EventRepo eventRepo;
    private final OutcomeRepo outcomeRepo;
    private final BetRepo betRepo;
    private final TransactionRepo transactionRepo;
    private final SettingService settingService;

    public EventServiceImpl(EventRepo eventRepo, OutcomeRepo outcomeRepo, PersonRepo personRepo, BetRepo betRepo,
                            TransactionRepo transactionRepo, SettingService settingService) {
        this.eventRepo = eventRepo;
        this.outcomeRepo = outcomeRepo;
        this.personRepo = personRepo;
        this.betRepo = betRepo;
        this.transactionRepo = transactionRepo;
        this.settingService = settingService;
    }

    @Override
    @Transactional
    public boolean add(String title, String description, Map<String, BigDecimal> outcomes) {
        Event event = new Event(title, description, null, new Date());
        eventRepo.save(event);

        List<Outcome> eventOutcomes = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : outcomes.entrySet()) {
            Outcome outcome = new Outcome(entry.getKey(), event, entry.getValue());
            outcomeRepo.save(outcome);
            eventOutcomes.add(outcome);
        }

        event.setOutcomes(eventOutcomes);
        eventRepo.save(event);

        return true;
    }

    @Override
    @Transactional
    public boolean setResult(Long eventId, Long outcomeId) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> {
            logger.error("Error while setting result for event: No event with ID {}", eventId);
            return new ResourceNotFoundException("Event", "id", eventId);
        });
        Outcome outcome = outcomeRepo.findById(outcomeId).orElseThrow(() -> {
            logger.error("Error while setting result for event: No outcome with ID {}", outcomeId);
            return new ResourceNotFoundException("Outcome", "id", outcomeId);
        });

        event.setWonOutcome(outcome);
        event.setEndDate(new Date());
        event.setIsEnded(true);
        eventRepo.save(event);

        BigDecimal newSystemVirtualMoneyValue = settingService.getSystemVirtualMoney();
        BigDecimal newSystemRealMoneyValue = settingService.getSystemRealMoney();

        for (Bet bet : event.getBets()) {
            if (bet.getOutcome().equals(outcome)) {
                bet.setStatus(BetStatus.WON);

                Person person = bet.getPerson();
                if (bet.getIsVirtualMoney()) {
                    person.setVirtualMoney(person.getVirtualMoney().add(bet.getMoney().multiply(bet.getCoefficient())));
                    newSystemVirtualMoneyValue = newSystemVirtualMoneyValue.subtract(bet.getMoney());
                } else {
                    person.setRealMoney(person.getRealMoney().add(bet.getMoney().multiply(bet.getCoefficient())));
                    newSystemRealMoneyValue = newSystemRealMoneyValue.subtract(bet.getMoney());
                }

                personRepo.save(person);
                WinTransaction transaction = new WinTransaction(person, bet.getIsVirtualMoney(), bet.getMoney(),
                        TransactionType.WIN, new Date(), bet);
//        transactionRepo.save(transaction);
            } else {
                bet.setStatus(BetStatus.LOSE);
            }
            betRepo.save(bet);
        }

        settingService.setSystemVirtualMoney(newSystemVirtualMoneyValue);
        settingService.setSystemRealMoney(newSystemRealMoneyValue);

        return true;
    }
}
