package com.market.repository;

import com.market.model.User;
import com.market.model.Wallet;
import com.market.security.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByWallet(Wallet wallet);

    Optional<User> findOneByUsername(String name);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByEmailLink(String emailLink);

    List<User> findAllByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role = 0")
    User findAdmin();
}
