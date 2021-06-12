package com.infinibet.service.impl;

import com.infinibet.config.SettingConstants;
import com.infinibet.exception.AppException;
import com.infinibet.model.Setting;
import com.infinibet.repository.SettingRepo;
import com.infinibet.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class SettingServiceImpl implements SettingService {

    private static final Logger logger = LoggerFactory.getLogger(BetServiceImpl.class);
    private final SettingRepo settingRepo;

    public SettingServiceImpl(SettingRepo settingRepo) {
        this.settingRepo = settingRepo;
    }

    @Override
    @Transactional
    public BigDecimal getSystemRealMoney() {
        Setting setting = settingRepo.findByKey(SettingConstants.REAL_MONEY).orElseThrow(() -> {
            logger.error("No Real Money in database");
            return new AppException("No Real Money in database");
        });

        return new BigDecimal(setting.getValue());
    }

    @Override
    @Transactional
    public void setSystemRealMoney(BigDecimal value) {
        Setting setting = settingRepo.findByKey(SettingConstants.REAL_MONEY).orElseThrow(() -> {
            logger.error("No Real Money in database");
            return new AppException("No Real Money in database");
        });

        setting.setValue(value.toString());
        settingRepo.save(setting);
    }

    @Override
    @Transactional
    public BigDecimal getSystemVirtualMoney() {
        Setting setting = settingRepo.findByKey(SettingConstants.VIRTUAL_MONEY).orElseThrow(() -> {
            logger.error("No Virtual Money in database");
            return new AppException("No Virtual Money in database");
        });

        return new BigDecimal(setting.getValue());
    }

    @Override
    @Transactional
    public void setSystemVirtualMoney(BigDecimal value) {
        Setting setting = settingRepo.findByKey(SettingConstants.VIRTUAL_MONEY).orElseThrow(() -> {
            logger.error("No Virtual Money in database");
            return new AppException("No Virtual Money in database");
        });

        setting.setValue(value.toString());
        settingRepo.save(setting);
    }
}
