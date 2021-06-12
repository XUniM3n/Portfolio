package com.market.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"buyer", "product"})
@Getter
@Setter
@ToString(exclude = {"buyer", "product"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn
    private User buyer;

    @OneToOne
    @JoinColumn
    private User seller;

    @OneToOne
    @JoinColumn
    private Product product;

    @NonNull
    private BigDecimal price;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
    @Column(name = "date_start")
    private Timestamp dateStart;
    @Column(name = "date_ship")
    private Timestamp dateShip;
    @Column(name = "date_end")
    private Timestamp dateEnd;

    public BigDecimal getPrice() {
        return price.stripTrailingZeros();
    }
}
