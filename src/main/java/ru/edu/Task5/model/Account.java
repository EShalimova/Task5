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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "account_pool_id", updatable = false)
    private BigInteger accountPoolId;

    @Column(name = "account_number", updatable = false)
    private String accountNumber;

    private Boolean bussy;
}
