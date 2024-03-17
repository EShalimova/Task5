package ru.edu.Task5.account;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class AccountData {
    private ArrayList<String> accountId;

    public AccountData() {
        this.accountId = new ArrayList<>();
    }
    public void setAccId(String id) {
        this.accountId.add( id);
    }
}
