package org.example.domain.repository.canonical;

import org.example.domain.canonical.Message;
import org.example.domain.canonical.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long> {
}
