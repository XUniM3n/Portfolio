package com.infinibet.runner;

import com.infinibet.model.Role;
import com.infinibet.model.RoleName;
import com.infinibet.repository.RoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class PopulateRoles implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(PopulateRoles.class);

    private final RoleRepo repo;

    public PopulateRoles(RoleRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public void run(String... strings) {
        if (!repo.findByName(RoleName.ROLE_PLAYER).isPresent()) {
            Role userRole = new Role(RoleName.ROLE_PLAYER);
            repo.save(userRole);
        }
        if (!repo.findByName(RoleName.ROLE_MODERATOR).isPresent()) {
            Role moderatorRole = new Role(RoleName.ROLE_MODERATOR);
            repo.save(moderatorRole);
        }
        if (!repo.findByName(RoleName.ROLE_ADMIN).isPresent()) {
            Role adminRole = new Role(RoleName.ROLE_ADMIN);
            repo.save(adminRole);
        }

        logger.info("Roles added to database");
    }
}
