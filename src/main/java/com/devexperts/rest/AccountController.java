package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.service.AccountServiceImpl;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountServiceImpl accountService;

    @PostMapping("/operations/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferAccountRequest request) {
        Double amount = request.getAmount();
        assertAmountIsCorrect(amount);
        assertAccountsArePresentedInRequest(request);

        Account source = accountService.getAccount(request.getSourceId());
        Account target = accountService.getAccount(request.getTargetId());
        assertAccountsAreFound(source, target);
        assertBalanceIsSufficient(source, amount);

        accountService.transfer(source, target, amount);
        return ResponseEntity.ok().build();
    }

    private void assertAmountIsCorrect(Double amount) {
        if (Objects.isNull(amount) || amount <= 0) {
            throw new BadAmountException();
        }
    }

    private void assertAccountsArePresentedInRequest(TransferAccountRequest request) {
        if (Objects.isNull(request.getSourceId()) || Objects.isNull(request.getTargetId())) {
            throw new AccountNotPresentedException();
        }
    }

    private void assertAccountsAreFound(Account source, Account target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new AccountsNotFoundException();
        }
    }

    private void assertBalanceIsSufficient(Account source, Double amount) {
        if (source.getBalance() < amount) {
            throw new InsufficientBalanceException();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class TransferAccountRequest {
        private Long sourceId;
        private Long targetId;
        private Double amount;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class BadAmountException extends RuntimeException {

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class AccountNotPresentedException extends RuntimeException {

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private class AccountsNotFoundException extends RuntimeException {
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private class InsufficientBalanceException extends RuntimeException {
    }
}
