package com.infinibet.dto;

import com.infinibet.model.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PersonDto {
    private Long id;
    private String username;
    private Set<String> roles;
    private BigDecimal realMoney;
    private BigDecimal virtualMoney;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.username = person.getUsername();
        this.roles = person.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        this.realMoney = person.getRealMoney();
        this.virtualMoney = person.getVirtualMoney();
    }
}
