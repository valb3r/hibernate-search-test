package org.example.hibernate_search_poc.config;

import lombok.SneakyThrows;
import org.example.domain.canonical.Message;
import org.example.domain.canonical.Transaction;
import org.example.domain.master.Account;
import org.example.hibernate_search_poc.service.TransactionsService;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;


@Component
public class HibernateSearchIndexer implements ApplicationListener<ApplicationReadyEvent> {

    private final EntityManagerFactory masterEntityManagerFactory;
    private final EntityManagerFactory canonicalEntityManagerFactory;


    public HibernateSearchIndexer(
            @Qualifier("masterEntityManagerFactory") EntityManagerFactory masterEntityManagerFactory,
            @Qualifier("canonicalEntityManagerFactory") EntityManagerFactory canonicalEntityManagerFactory
    ) {
        this.masterEntityManagerFactory = masterEntityManagerFactory;
        this.canonicalEntityManagerFactory = canonicalEntityManagerFactory;
    }

    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationReadyEvent event) {
        SearchSession searchSession = Search.session(masterEntityManagerFactory.createEntityManager());
        MassIndexer indexer = searchSession.massIndexer( Account.class ).threadsToLoadObjects( 1 );
        indexer.startAndWait();

        searchSession = Search.session(canonicalEntityManagerFactory.createEntityManager());
        indexer = searchSession.massIndexer(Message.class, Transaction.class).threadsToLoadObjects( 1 );
        indexer.startAndWait();
    }
}
