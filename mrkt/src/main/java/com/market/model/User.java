package com.market.model;

import com.market.security.role.Role;
import com.market.security.state.UserState;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private BigDecimal balance;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Wallet wallet;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private Role role;
    @Enumerated(EnumType.ORDINAL)
    private UserState state;
    @Length(max = 100)
    private String contacts;
    @Column(name = "reg_date")
    private Timestamp regDate;
    @Column(name = "email_link")
    private String emailLink;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seller")
    private List<Product> sellingProducts;
    // Orders, where this user is buyer
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "buyer")
    private List<Order> bOrders;
    // Orders, where this user is seller
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seller")
    private List<Order> sOrders;

    public BigDecimal getBalance() {
        return balance.stripTrailingZeros();
    }
}