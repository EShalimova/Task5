package ru.edu.Task5.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.Task5.repo.AccountRepo;
import ru.edu.Task5.model.Account;
import ru.edu.Task5.model.TppProduct;
import ru.edu.Task5.model.TppProductRegister;

@Service
public class RegisterService {
    @Transactional
    public void setProduct(TppProduct tppProduct, AccountRepo accountRepo) {
        for (TppProductRegister register : tppProduct.getRegisters())
            if (register.isCheckNew() && (register.getAccount() != null)) {
                Account account = accountRepo.findFirstById(register.getAccount());
                register.setAccountNumber(account.getAccountNumber());
                account.setBussy(true);
                accountRepo.save(account);
            }
    }
}
