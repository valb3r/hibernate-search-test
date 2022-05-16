package org.example.domain.repository.canonical;

import org.example.domain.canonical.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long> {

    @Query("FROM Transaction t WHERE t.accountIbanFrom = ?1 OR t.accountIbanTo = ?1")
    List<Transaction> findByIban(String iban);
}
