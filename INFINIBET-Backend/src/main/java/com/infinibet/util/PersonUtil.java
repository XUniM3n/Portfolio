package com.infinibet.util;

import com.infinibet.model.Person;
import com.infinibet.repository.PersonRepo;
import com.infinibet.security.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersonUtil {

    private final PersonRepo personRepo;

    public PersonUtil(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public Person getCurrentPerson() {
        Optional<String> loginOptional = SecurityUtils.getCurrentPersonLogin();
        if (!loginOptional.isPresent())
            return null;
        String login = loginOptional.get();
        Optional<Person> personOptional = personRepo.findByUsername(login);
        return personOptional.orElse(null);
    }
}
