package org.example.domain.repository.master;

import org.example.domain.master.Account;
import org.example.hibernate_search_poc.service.AccountsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {

    Account findByIban(String iban);
}
