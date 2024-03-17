package ru.edu.Task5.instance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
public class Agreement {
    @JsonProperty("GeneralAgreementId")
    @JsonSetter("GeneralAgreementId")
    String generalAgreementId;
    @JsonProperty("SupplementaryAgreementId")
    @JsonSetter("SupplementaryAgreementId")
    String supplementaryAgreementId;
    String arrangementType;
    BigInteger shedulerJobId;
    @JsonProperty("Number")
    @JsonSetter("Number")
    String number;
    Date openingDate;
    Date closingDate;
    @JsonProperty("CancelDate")
    @JsonSetter("CancelDate")
    Date cancelDate;
    BigInteger validityDuration;
    String cancellationReason;
    @JsonProperty("Status")
    @JsonSetter("Status")
    String status;
    Date interestCalculationDate;
    Double interestRate;
    Double coefficient;
    String coefficientAction;
    Double minimumInterestRate;
    Double minimumInterestRateCoefficient;
    String minimumInterestRateCoefficientAction;
    Double maximalInterestRate;
    Double maximalInterestRateCoefficient;
    String maximalInterestRateCoefficientAction;
}
