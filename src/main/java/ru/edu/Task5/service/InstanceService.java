package ru.edu.Task5.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.edu.Task5.instance.Agreement;
import ru.edu.Task5.instance.InstanceCreate;
import ru.edu.Task5.instance.InstanceData;
import ru.edu.Task5.instance.InstanceResult;
import ru.edu.Task5.model.AgreementModel;
import ru.edu.Task5.model.TppProduct;
import ru.edu.Task5.model.TppProductRegister;
import ru.edu.Task5.repo.AccountRepo;
import ru.edu.Task5.repo.AgreementRepo;
import ru.edu.Task5.repo.TppProductRepo;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Set;

@Service
public class InstanceService {
    private TppProductRepo tppProductRepo;

    private AgreementRepo agreementRepo;

    private AccountRepo accountRepo;

    private ClientFromMdm clientMdm;

    @Autowired
    public void setTppProductRepo(TppProductRepo tppProductRepo) {
        this.tppProductRepo = tppProductRepo;
    }

    @Autowired
    public void setAgreementRepo(AgreementRepo agreementRepo) {
        this.agreementRepo = agreementRepo;
    }

    @Autowired
    public void setAccountRepo(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Autowired
    public void setClientMdm(ClientFromMdm clientMdm) {
        this.clientMdm = clientMdm;
    }

    @Getter @Setter
    String message;

    public BigInteger getClientIdForMdm(String mdmCode) {
        BigInteger result = clientMdm.getClientIdForMdm( mdmCode);
        return result;
    }

    public void setInstance( TppProduct tppProduct) throws SQLException  {
        try {
            RegisterService productService = new RegisterService();
            productService.setProduct( tppProduct, accountRepo);
            tppProductRepo.save(tppProduct);

        }
        catch (Exception ex) {
            throw new SQLException( ex);
        }
    }

    public int setProduct(InstanceCreate instanceCreate, InstanceResult instanceResult) {
        TppProduct tppProduct = new TppProduct();
        BigInteger productCodeId;
        boolean isFind;

        try {
            if (instanceCreate.getInstanceId() != null) {
                tppProduct = tppProductRepo.findFirstById( instanceCreate.getInstanceId() );
                if (tppProduct == null) {
                    this.message = "Отсутствует договор с id = " + instanceCreate.getInstanceId();
                    return 400;
                }
            }
            else {
                BigInteger clientId = getClientIdForMdm(instanceCreate.getMdmCode());

                if ( clientId.longValue() == 99999L ) {
                    this.message = "Не найден клиент по МДМ-коду " + instanceCreate.getMdmCode();
                    return 400;
                }

                TppProduct tppExists = tppProductRepo.findFirstByContNumber(instanceCreate.getContractNumber());
                if (tppExists != null) {
                    this.message = "Номер договора " + instanceCreate.getContractNumber() + " уже существует для ЭП с ИД " + tppExists.getId();
                    return 400;
                }

                productCodeId = accountRepo.getProductId( instanceCreate.getProductCode());
                if (productCodeId == null) {
                    this.message = "Ошибка при получении типа продукта по коду " + instanceCreate.getProductCode();
                    return 400;
                }

                tppProduct.setProductCodeId( productCodeId);
                tppProduct.setClientId(clientId);
                tppProduct.setProductType(instanceCreate.getProductType());
                tppProduct.setContNumber(instanceCreate.getContractNumber());
                tppProduct.setPriority(instanceCreate.getPriority());
                tppProduct.setDateOfConclusion(instanceCreate.getContractDate());
                tppProduct.setPenaltyRate(instanceCreate.getInterestRatePenalty());
                tppProduct.setThresholdAmount(instanceCreate.getThresholdAmount());
                tppProduct.setInterestRateType(instanceCreate.getRateType());
                tppProduct.setTaxRate(instanceCreate.getTaxPercentageRate());

                Set<BigInteger> accountIdSet = accountRepo.getAccountId(instanceCreate.getBranchCode(), instanceCreate.getIsoCurrencyCode(),
                        instanceCreate.getMdmCode(), String.format("%02d", instanceCreate.getPriority()), instanceCreate.getRegisterType());
                isFind = false;
                for (BigInteger accountId : accountIdSet) {
                    isFind = true;
                    tppProduct.insertRegister(new TppProductRegister(instanceCreate.getRegisterType(), accountId, instanceCreate.getIsoCurrencyCode(), "OPEN", ""));
                    break;
                }
                if (!isFind) {
                    this.message = "Ошибка при получении инфо из пула счетов для типа регистра " + instanceCreate.getRegisterType();
                    return 400;
                }
            }
            //доп.соглашения
            for ( Agreement agreement : instanceCreate.getInstanceAgreement() ) {
                AgreementModel agreementExists = agreementRepo.findFirstByNumber( agreement.getNumber() );
                if (agreementExists != null) {
                    this.message = "Параметр № Дополнительного соглашения (сделки) Number " + agreement.getNumber() + " уже существует для ЭП с ИД " + agreementExists.getProductId();
                    return 400;
                }
                tppProduct.insertAgreement( new AgreementModel(
                        agreement.getGeneralAgreementId(), agreement.getSupplementaryAgreementId(), agreement.getArrangementType(), agreement.getShedulerJobId(),
                        agreement.getNumber(), agreement.getOpeningDate(), agreement.getClosingDate(), agreement.getCancelDate(),
                        agreement.getValidityDuration(), agreement.getCancellationReason(), agreement.getStatus(),
                        agreement.getInterestCalculationDate(), agreement.getInterestRate(), agreement.getCoefficient(), agreement.getCoefficientAction(),
                        agreement.getMinimumInterestRate(), agreement.getMinimumInterestRateCoefficient(), agreement.getMinimumInterestRateCoefficientAction(),
                        agreement.getMaximalInterestRate(), agreement.getMaximalInterestRateCoefficient(), agreement.getMaximalInterestRateCoefficientAction()));
            }

            try {
                setInstance( tppProduct);

                InstanceData instanceData = new InstanceData();

                instanceData.setInstanceId( tppProduct.getId().toString());

                for ( AgreementModel agreements : tppProduct.getAgreements())
                    if (agreements.isCheckNew())
                        instanceData.setAgrId( agreements.getId().toString());

                for ( TppProductRegister regs : tppProduct.getRegisters())
                    if (regs.isCheckNew())
                        instanceData.setRegId( regs.getId().toString() );

                instanceResult.setData( instanceData);

                return 200;
            }
            catch (Exception ex) {
                this.message = ex.getMessage();
                return 500;
            }
        }
        catch (Exception ex) {
            this.message = ex.getMessage();
            return 500;
        }
    }
}
