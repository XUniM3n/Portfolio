package com.infinibet.controller;

import com.infinibet.config.AppConstants;
import com.infinibet.dto.BetDto;
import com.infinibet.exception.ResourceNotFoundException;
import com.infinibet.exception.UnauthorizedException;
import com.infinibet.model.Bet;
import com.infinibet.model.Person;
import com.infinibet.payload.ApiResponse;
import com.infinibet.payload.BetRequest;
import com.infinibet.payload.SuccessResponse;
import com.infinibet.repository.BetRepo;
import com.infinibet.repository.PersonRepo;
import com.infinibet.security.AuthoritiesConstants;
import com.infinibet.security.SecurityUtils;
import com.infinibet.service.BetService;
import com.infinibet.util.PersonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/bets")
@RestController
public class BetController {

    private final BetService betService;
    private final PersonUtil personUtil;
    private final BetRepo betRepo;
    private final PersonRepo personRepo;

    public BetController(BetService betService, PersonUtil personUtil, BetRepo betRepo, PersonRepo personRepo) {
        this.betService = betService;
        this.personUtil = personUtil;
        this.betRepo = betRepo;
        this.personRepo = personRepo;
    }

    @GetMapping("/{id}")
    public BetDto getBetById(@PathVariable(value = "id") Long id) {
        Bet bet = betRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Bet", "id", id));

        // User should be able access only his bets
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PLAYER) &&
                !personUtil.getCurrentPerson().getId().equals(bet.getPerson().getId())) {
            throw new UnauthorizedException();
        }

        return new BetDto(bet);
    }

    @GetMapping("")
    public List<BetDto> getEventsBy(@RequestParam(value = "page") Integer page,
                                    @RequestParam(value = "size") Integer size,
                                    @RequestParam(value = "username") String username) {
        if (page == null)
            page = AppConstants.DEFAULT_PAGE_NUMBER;
        if (size == null)
            size = AppConstants.DEFAULT_PAGE_SIZE;

        Person person;
        if (username == null) {
            person = personUtil.getCurrentPerson();
        } else {
            Optional<Person> personOptional = personRepo.findByUsername(username);
            if (!personOptional.isPresent())
                throw new ResourceNotFoundException("Person", "username", username);
            person = personOptional.get();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Bet> betsPage = betRepo.findAllByPerson(pageable, person);
        List<Bet> bets = betsPage.getContent();
        return bets.stream().map(BetDto::new).collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ApiResponse add(@RequestBody @Valid BetRequest betRequest) {
        betService.add(personUtil.getCurrentPerson(), betRequest.getEventId(), betRequest.getOutcomeId(),
                betRequest.getIsVirtualMoney(), betRequest.getMoney());

        return new SuccessResponse("Bet added");
    }
}
