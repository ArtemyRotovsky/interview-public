package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.utils.TriConsumer;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@EqualsAndHashCode
public class AccountServiceImpl {
    private final ConcurrentHashMap<AccountKey, Account> accounts = new ConcurrentHashMap<>();
    private static final Object tieLock = new Object();

    public void clear() {
        accounts.clear();
    }

    public void createAccount(Account account) {
        if (accounts.get(account.getAccountKey()) != null) {
            throw new AccountAlreadyExistsException();
        }
        accounts.put(account.getAccountKey(), account);
    }

    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void transfer(Account source, Account target, double amount) {

        TriConsumer<Account, Account, Double> transfer = (src, trg, am) -> {
            if (src.getBalance().compareTo(am) < 0)
                throw new InsufficientAccountBalanceException();
            else {
                src.debit(am);
                trg.credit(am);
            }
        };

        int fromHash = source.hashCode();
        int toHash = target.hashCode();
        if (fromHash < toHash) {
            synchronized (source) {
                synchronized (target) {
                    transfer.accept(source, target, amount);
                }
            }
        } else if (fromHash > toHash) {
            synchronized (target) {
                synchronized (source) {
                    transfer.accept(source, target, amount);
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (source) {
                    synchronized (target) {
                        transfer.accept(source, target, amount);
                    }
                }
            }
        }
    }

    private static class InsufficientAccountBalanceException extends RuntimeException {
    }

    static class AccountAlreadyExistsException extends RuntimeException {
    }
}
