package com.devexperts.account;

import lombok.*;

@Builder
@Getter
@ToString
public class Account {
    private AccountKey accountKey;
    private String firstName;
    private String lastName;
    @Setter
    private Double balance;

    public void debit(double amount) {
        balance -= amount;
    }

    public void credit(double amount) {
        balance += amount;
    }

}
