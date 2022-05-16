package org.example.hibernate_search_poc.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.canonical.Transaction;
import org.example.domain.master.Account;
import org.example.domain.repository.canonical.MessagesRepository;
import org.example.domain.repository.canonical.TransactionsRepository;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
@Transactional(transactionManager = "canonicalTransactionManager")
@RequiredArgsConstructor
public class TransactionsService {

    private final EntityManager entityManager;
    private final MessagesRepository messages;
    private final TransactionsRepository transactions;

    public List<Transaction> allTransactions(long messageById) {
        var message = messages.findById(messageById).get();
        return message.getTransactions();
    }

    public void save(long messageById, Transaction transaction) {
        var message = messages.findById(messageById).get();
        message.addTransaction(transaction);
        messages.save(message);
    }

    public void delete(long id) {
        transactions.deleteById(id);
    }

    /**
     * Account is persisted in database and we create new transaction same as with MQ systems.
     */
    @TransactionalEventListener
    public void updateAccount(Account account) {
        SearchSession searchSession = Search.session(entityManager);
        SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
        // Actually it should be done in batches - https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#mapper-orm-indexing-manual-indexingplan-process-execute
        transactions.findByIban(account.getIban()).forEach(indexingPlan::addOrUpdate);
        indexingPlan.execute();
    }
}
