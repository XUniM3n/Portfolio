package com.market.security.details;

import com.market.model.User;
import com.market.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findOneByUsername(username);
        if (!user.isPresent())
            user = userRepository.findOneByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new UserDetailsImpl(user.get());
    }
}
