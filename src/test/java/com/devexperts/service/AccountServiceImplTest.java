package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceImplTest {
    private AccountServiceImpl accountService = new AccountServiceImpl();

    @Test
    void createAccount() {
        Account account = Account.builder()
                .accountKey(AccountKey.builder()
                        .accountId(2L)
                        .build())
                .balance(1000D)
                .build();

        accountService.createAccount(account);
        assertThrows(AccountServiceImpl.AccountAlreadyExistsException.class, () -> accountService.createAccount(account));
    }
}
