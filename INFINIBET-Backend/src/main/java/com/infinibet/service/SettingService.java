package com.infinibet.service;

import java.math.BigDecimal;

public interface SettingService {
    BigDecimal getSystemRealMoney();

    void setSystemRealMoney(BigDecimal value);

    BigDecimal getSystemVirtualMoney();

    void setSystemVirtualMoney(BigDecimal value);
}
