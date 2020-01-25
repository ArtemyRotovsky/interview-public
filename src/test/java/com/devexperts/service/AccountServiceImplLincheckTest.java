package com.devexperts.service;

import com.devexperts.account.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlinx.lincheck.LinChecker;
import org.jetbrains.kotlinx.lincheck.LoggingLevel;
import org.jetbrains.kotlinx.lincheck.Options;
import org.jetbrains.kotlinx.lincheck.annotations.Operation;
import org.jetbrains.kotlinx.lincheck.annotations.Param;
import org.jetbrains.kotlinx.lincheck.paramgen.LongGen;
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressCTest;
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions;
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState;
import org.junit.Test;

/*
* note: clarified with Nikita Koval that passing parameters with custom type to test methods will not work
* todo: change methods signature to get primitive types (just account's id's)
* */
@StressCTest
public class AccountServiceImplLincheckTest extends VerifierState {
    private AccountServiceImpl accountService = new AccountServiceImpl();

    @Operation
    public Account getAccount(@Param(gen = LongGen.class) Long id) {
        return accountService.getAccount(id);
    }

    /*@Operation(handleExceptionsAsResult = AccountServiceImpl.AccountAlreadyExistsException.class)
    public void createAccount(@Param(gen = AccountGenerator.class) Account account) {
        accountService.createAccount(account);
    }*/

    /*@Operation
    public void transfer(@Param(gen = AccountGenerator.class) Account source,
                         @Param(gen = AccountGenerator.class) Account target,
                         @Param(gen = DoubleGen.class) Double amount) {
        accountService.transfer(source, target, amount);
    }*/

    @Test
    public void test() {
        Options opts = new StressOptions()
                .iterations(10)
                .logLevel(LoggingLevel.INFO);
        LinChecker.check(AccountServiceImplLincheckTest.class, opts);
    }

    @NotNull
    @Override
    protected Object extractState() {
        return accountService;
    }


}
