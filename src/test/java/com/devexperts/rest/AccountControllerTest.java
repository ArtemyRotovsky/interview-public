package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private AccountServiceImpl accountService;

    @Before
    public void init() {
        Account source = Account.builder()
                .accountKey(AccountKey.builder()
                        .accountId(1L)
                        .build())
                .balance(1000.)
                .build();

        Account target = Account.builder()
                .accountKey(AccountKey.builder()
                        .accountId(2L)
                        .build())
                .balance(0.)
                .build();

        accountService.createAccount(source);
        accountService.createAccount(target);
    }

    @After
    public void clear() {
        accountService.clear();
    }

    @Test
    public void successTransfer() {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/accounts/operations/transfer",
                AccountController.TransferAccountRequest.builder()
                        .sourceId(1L)
                        .targetId(2L)
                        .amount(300.)
                        .build(),
                Void.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(accountService.getAccount(1L).getBalance(), Double.valueOf(700));
        Assert.assertEquals(accountService.getAccount(2L).getBalance(), Double.valueOf(300));
    }

    @Test
    public void notSuccessTransfer() {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/accounts/operations/transfer",
                AccountController.TransferAccountRequest.builder()
                        .sourceId(1L)
                        .targetId(2L)
                        .amount(1100.)
                        .build(),
                Void.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void badRequest() {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/accounts/operations/transfer",
                AccountController.TransferAccountRequest.builder()
                        .sourceId(null)
                        .targetId(null)
                        .amount(null)
                        .build(),
                Void.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void accountNotFound() {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/accounts/operations/transfer",
                AccountController.TransferAccountRequest.builder()
                        .sourceId(1L)
                        .targetId(3L)
                        .amount(100.)
                        .build(),
                Void.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
