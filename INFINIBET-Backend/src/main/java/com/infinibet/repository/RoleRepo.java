package com.infinibet.repository;

import com.infinibet.model.Role;
import com.infinibet.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName user);
}
