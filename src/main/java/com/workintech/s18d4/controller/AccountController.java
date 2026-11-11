package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping({"/account", "/accounts"})
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    public AccountController(
            AccountService accountService,
            CustomerService customerService) {

        this.accountService = accountService;
        this.customerService = customerService;
    }

    private AccountResponse toResponse(Account account) {
        Long customerId = account.getCustomer() != null
                ? account.getCustomer().getId()
                : null;

        return new AccountResponse(
                account.getId(),
                account.getAccountName(),
                account.getMoneyAmount(),
                customerId
        );
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {

        List<AccountResponse> accounts = accountService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> find(@PathVariable Long id) {

        Account account = accountService.find(id);

        if (account == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Account not found: " + id
            );
        }

        return ResponseEntity.ok(toResponse(account));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<AccountResponse> save(
            @PathVariable Long customerId,
            @RequestBody Account account) {

        Customer customer = customerService.find(customerId);

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + customerId
            );
        }

        account.setCustomer(customer);

        Account savedAccount = accountService.save(account);

        return ResponseEntity.ok(toResponse(savedAccount));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<AccountResponse> update(
            @PathVariable Long customerId,
            @RequestBody Account account) {

        Customer customer = customerService.find(customerId);

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + customerId
            );
        }

        Account existingAccount = accountService.find(account.getId());

        if (existingAccount == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Account not found: " + account.getId()
            );
        }

        existingAccount.setAccountName(account.getAccountName());
        existingAccount.setMoneyAmount(account.getMoneyAmount());
        existingAccount.setCustomer(customer);

        Account updatedAccount = accountService.save(existingAccount);

        return ResponseEntity.ok(toResponse(updatedAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountResponse> delete(@PathVariable Long id) {

        Account account = accountService.find(id);

        if (account == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Account not found: " + id
            );
        }

        Account deletedAccount = accountService.delete(id);

        return ResponseEntity.ok(toResponse(deletedAccount));
    }
}