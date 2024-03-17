package ru.edu.Task5.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.edu.Task5.account.AccountCreate;
import ru.edu.Task5.account.AccountData;
import ru.edu.Task5.account.AccountResult;
import ru.edu.Task5.enums.AccountState;
import ru.edu.Task5.model.*;
import ru.edu.Task5.repo.AccountRepo;
import ru.edu.Task5.repo.TppProductRepo;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Set;

@Service
public class AccountService {
    private TppProductRepo tppProductRepo;
    private AccountRepo accountRepo;

    @Autowired
    public void setTppProductRepo(TppProductRepo tppProductRepo) {
        this.tppProductRepo = tppProductRepo;
    }

    @Autowired
    public void setAccountRepo(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Getter
    @Setter
    String message;

    public void setRegistry( TppProduct tppProduct) throws SQLException {
        try {
            RegisterService productService = new RegisterService();
            productService.setProduct( tppProduct, accountRepo);
            tppProductRepo.save(tppProduct);

        }
        catch (Exception ex) {
            throw new SQLException( ex);
        }
    }
    public int setAccount(AccountCreate accountCreate, AccountResult accountResult) {
        TppProduct tppProduct;
        boolean isFind;

        tppProduct = tppProductRepo.findFirstById( accountCreate.getInstanceId() );
        if (tppProduct == null) {
            this.message = "Отсутствует договор с id = " + accountCreate.getInstanceId();
            return 400;
        }

        for ( TppProductRegister productRegister : tppProduct.getRegisters()) {
            if ( productRegister.getRegisterValue().equals( accountCreate.getRegistryTypeCode())) {
                this.message = "Параметр registryTypeCode тип регистра " + accountCreate.getRegistryTypeCode() + " уже существует для ЭП с ИД  " + accountCreate.getInstanceId();
                return 400;
            }
        }

        Set<BigInteger> accountIdSet = accountRepo.getAccountId(accountCreate.getBranchCode(), accountCreate.getCurrencyCode(),
                accountCreate.getMdmCode(), accountCreate.getPriorityCode(), accountCreate.getRegistryTypeCode());
        isFind = false;
        for (BigInteger accId : accountIdSet) {
            isFind = true;
            tppProduct.insertRegister(new TppProductRegister(accountCreate.getRegistryTypeCode(), accId, accountCreate.getCurrencyCode(), AccountState.OPENED.name(), ""));
            break;
        }
        if (!isFind) {
            this.message = "Ошибка при получении инфо из пула счетов для типа регистра " + accountCreate.getRegistryTypeCode();
            return 400;
        }

        try {
            setRegistry( tppProduct);

            AccountData accountData = new AccountData();

            for ( TppProductRegister re : tppProduct.getRegisters())
                if (re.isCheckNew())
                    accountData.setAccId( re.getId().toString() );

            accountResult.setData( accountData);

            return 200;
        }
        catch (Exception ex) {
            this.message = ex.getMessage();
            return 500;
        }

    }
}