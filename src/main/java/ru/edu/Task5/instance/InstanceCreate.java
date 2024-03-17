package ru.edu.Task5.instance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.edu.Task5.instance.additional.AdditionalPropData;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
@Getter
@Setter
public class InstanceCreate {
    private BigInteger instanceId;
    @NotEmpty(message = "Null productType")
    private String productType;
    @NotEmpty(message = "productCode is empty")
    private String productCode;
    @NotEmpty(message = "registerType is empty")
    private String registerType;
    @NotEmpty(message = "mdmCode is empty")
    private String mdmCode;
    @NotEmpty(message = "contractNumber is empty")
    private String contractNumber;
    @NotNull(message = "contractDate is null")
    private Date contractDate;
    @NotNull(message = "priority is null")
    private Integer priority;
    private Double interestRatePenalty;
    private Double minimalBalance;
    private Double thresholdAmount;
    private String accountingDetails;
    private String rateType;
    private Double taxPercentageRate;
    private Double technicalOverdraftLimitAmount;
    private Long contractId;
    @JsonProperty("BranchCode")
    @JsonSetter("BranchCode")
    private String branchCode;
    @JsonProperty("IsoCurrencyCode")
    @JsonSetter("IsoCurrencyCode")
    private String isoCurrencyCode;
    private String urgencyCode;
    @JsonProperty("ReferenceCode")
    @JsonSetter("ReferenceCode")
    private Long referenceCode;
    private AdditionalPropData additionalPropertiesVip;
    private ArrayList<Agreement> instanceAgreement;
}
