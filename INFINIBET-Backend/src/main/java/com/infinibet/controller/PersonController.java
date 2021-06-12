package com.infinibet.controller;

import com.infinibet.dto.PersonDto;
import com.infinibet.model.Person;
import com.infinibet.util.PersonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/persons")
@RestController
public class PersonController {
    private final PersonUtil personUtil;

    public PersonController(PersonUtil personUtil) {
        this.personUtil = personUtil;
    }

    @GetMapping("/me")
    public PersonDto getPersonProfile() {
        Person person = personUtil.getCurrentPerson();
        return new PersonDto(person);
    }
}
