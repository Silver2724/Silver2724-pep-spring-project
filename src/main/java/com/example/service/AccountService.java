package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> register(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        //check if the register details are valid
        if(account == null || username == null || password == null ||
           username.trim().isEmpty() || password.trim().isEmpty() || 
           username.isBlank() || password.length() < 4) {
            return Optional.empty();
        }

        return Optional.of(accountRepository.save(account));
    }

    //check if username already exists
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public Optional<Account> login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        //check if the login details are valid
        if(account == null || username == null || password == null ||
           username.trim().isEmpty() || password.trim().isEmpty() || 
           username.isBlank() || password.length() < 4) {
            return Optional.empty();
        }

        //check if account exists with the given username
        Optional<Account> existingAccount = accountRepository.findByUsername(username);

        //check if the existing account matches the password
        if(existingAccount.isPresent()) {
            if(existingAccount.get().getPassword().equals(password)) {
                return existingAccount;
            }
        }

        return Optional.empty();
    }
}
