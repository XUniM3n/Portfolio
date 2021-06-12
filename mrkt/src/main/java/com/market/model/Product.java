package com.market.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "seller")
@Getter
@Setter
@ToString(exclude = "seller")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private City city;

    private String name;

    private String description;

    private String image;

    private boolean available;

    @Column(name = "date_created")
    private Timestamp dateCreated;

    private BigDecimal price;

    public BigDecimal getPrice() {
        return price.stripTrailingZeros();
    }

}