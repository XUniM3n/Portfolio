package com.infinibet.runner;

import com.infinibet.config.SettingConstants;
import com.infinibet.model.Setting;
import com.infinibet.repository.SettingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Component
public class PopulateSettings implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(PopulateSettings.class);

    private final SettingRepo repo;

    public PopulateSettings(SettingRepo repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public void run(String... strings) {
        if (!repo.findByKey(SettingConstants.REAL_MONEY).isPresent()) {
            Setting settings = Setting.builder()
                    .key(SettingConstants.REAL_MONEY).value(new BigDecimal(0).toString()).build();
            repo.save(settings);
        }

        if (!repo.findByKey(SettingConstants.VIRTUAL_MONEY).isPresent()) {
            Setting settings = Setting.builder()
                    .key(SettingConstants.VIRTUAL_MONEY).value(new BigDecimal(0).toString()).build();
            repo.save(settings);
        }

        logger.info("Settings populated");
    }
}

