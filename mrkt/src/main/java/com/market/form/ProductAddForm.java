package com.market.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductAddForm {

    private String name;

    private BigDecimal price;

    private Long city;

    private String description;
}
