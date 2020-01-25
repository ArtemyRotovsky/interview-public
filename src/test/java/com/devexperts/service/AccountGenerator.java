package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.jetbrains.kotlinx.lincheck.paramgen.DoubleGen;
import org.jetbrains.kotlinx.lincheck.paramgen.LongGen;
import org.jetbrains.kotlinx.lincheck.paramgen.ParameterGenerator;
import org.jetbrains.kotlinx.lincheck.paramgen.StringGen;

public class AccountGenerator implements ParameterGenerator<Account> {

    public AccountGenerator(String configuration) {

    }

    @Override
    public Account generate() {
        return Account.builder()
                .accountKey(generateAccountKey())
                .firstName(generateName())
                .lastName(generateName())
                .balance(generateBalance())
                .build();
    }

    private AccountKey generateAccountKey() {
        return  AccountKey.builder()
                .accountId(new LongGen("").generate())
                .build();
    }

    private String generateName() {
        return new StringGen("").generate();
    }

    private Double generateBalance() {
        return new DoubleGen("").generate();
    }
}
