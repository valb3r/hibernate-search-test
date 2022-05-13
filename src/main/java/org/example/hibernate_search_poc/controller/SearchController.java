package org.example.hibernate_search_poc.controller;

import org.example.domain.canonical.Transaction;
import org.example.domain.master.Account;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
@RequestMapping(path = "/search")
public class SearchController {

    private final EntityManager masterManager;
    private final EntityManager canonicalManager;

    public SearchController(
            @Qualifier("masterEntityManagerFactory") EntityManager masterManager,
            @Qualifier("canonicalEntityManagerFactory") EntityManager canonicalManager
    ) {
        this.masterManager = masterManager;
        this.canonicalManager = canonicalManager;
    }

    @Transactional(transactionManager = "canonicalTransactionManager")
    @GetMapping("/transactions")
    public List<Transaction> findTransactions(String query) {
        SearchSession searchSession = Search.session( canonicalManager );

        SearchResult<Transaction> result = searchSession.search( Transaction.class )
                .where( f -> f.simpleQueryString()
                        .field( "accountIbanFrom" )
                        .matching( query ) )
                .fetch( 20 );

        return result.hits();
    }

    @Transactional(transactionManager = "masterTransactionManager")
    @GetMapping("/accounts")
    public List<Account> findAccounts(String query) {
        SearchSession searchSession = Search.session( masterManager );

        SearchResult<Account> result = searchSession.search( Account.class )
                .where( f -> f.simpleQueryString()
                        .field( "name" )
                        .matching( query ) )
                .fetch( 20 );

        return result.hits();
    }
}
