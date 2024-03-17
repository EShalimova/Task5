package ru.edu.Task5.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tpp_product_register")
public class TppProductRegister {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private BigInteger id;


    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "product_id", nullable = true, updatable = false)
    private TppProduct productId;

    @Column(name = "type", updatable = false)
    private String registerValue;

    @Column(name = "account", updatable = false)
    private BigInteger account;

    @Column(name = "currency_code", updatable = false)
    private String currencyCode;

    @Column(name = "state", updatable = false)
    private String state;

    @Column(name = "account_number")
    private String accountNumber;

    transient private boolean checkNew; //=true - создано сейчас

    public TppProductRegister(String registerValue, BigInteger account, String currencyCode, String state, String accountNumber) {
        this.registerValue = registerValue;
        this.account = account;
        this.currencyCode = currencyCode;
        this.state = state;
        this.accountNumber = accountNumber;
        this.checkNew = true;
    }
}
