package org.example.hibernate_search_poc.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.master.Account;
import org.example.domain.repository.master.AccountsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(transactionManager = "masterTransactionManager")
@RequiredArgsConstructor
public class AccountsService {

    private final AccountsRepository accounts;

    public Account findByIban(String iban) {
        return accounts.findByIban(iban);
    }

    public List<Account> allAccounts() {
        return accounts.findAll();
    }

    public void save(Account account) {
        accounts.save(account);
    }

    public void delete(long id) {
        accounts.deleteById(id);
    }
}
