package com.infinibet.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BetRequest {
    @NotNull
    private Long eventId;
    @NotNull
    private Long outcomeId;
    @NotNull
    private BigDecimal money;
    @NotNull
    private Boolean isVirtualMoney;
}
