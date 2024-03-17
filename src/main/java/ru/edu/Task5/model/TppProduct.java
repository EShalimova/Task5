package ru.edu.Task5.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tpp_product")
public class TppProduct {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "product_code_id")
    private BigInteger productCodeId;

    @Column(name = "client_id")
    private BigInteger clientId;

    @Column(name = "type")
    private String productType;

    @Column(name = "number")
    private String contNumber;

    private Integer priority;

    @Column(name = "date_of_conclusion")
    private Date dateOfConclusion;

    @Column(name = "penalty_rate")
    private Double penaltyRate;

    @Column(name = "threshold_amount")
    private Double thresholdAmount;

    @Column(name = "requisite_type")
    private String requisiteType;

    @Column(name = "interest_rate_type")
    private String interestRateType;

    @Column(name = "tax_rate")
    private Double taxRate;

    @OneToMany( mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TppProductRegister> registers = new ArrayList<>();

    public void insertRegister(TppProductRegister register) {
        registers.add(register);
        register.setProductId( this);
    }

    @OneToMany( mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementModel> agreements = new ArrayList<>();

    public void insertAgreement(AgreementModel agreement) {
        agreements.add(agreement);
        agreement.setProductId( this);
    }
}
