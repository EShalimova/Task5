package ru.edu.Task5.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResult {
    private String message;
    private AccountData data;

    public AccountResult() {
        data = new AccountData();
    }
}
