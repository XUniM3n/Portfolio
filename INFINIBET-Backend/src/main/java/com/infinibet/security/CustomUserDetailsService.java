package com.infinibet.security;

import com.infinibet.model.Person;
import com.infinibet.repository.PersonRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PersonRepo personRepo;

    public CustomUserDetailsService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Let people login with either username or email
        Person person = personRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));

        return UserPrincipal.create(person);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        Person person = personRepo.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id));

        return UserPrincipal.create(person);
    }
}
