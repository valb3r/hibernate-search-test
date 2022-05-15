package org.example.hibernate_search_poc.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.canonical.Transaction;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class SearchService {

    private final EntityManager masterManager;
    private final EntityManager canonicalManager;

    public SearchService(
            @Qualifier("masterEntityManagerFactory") EntityManager masterManager,
            @Qualifier("canonicalEntityManagerFactory") EntityManager canonicalManager
    ) {
        this.masterManager = masterManager;
        this.canonicalManager = canonicalManager;
    }

    @Transactional(transactionManager = "canonicalTransactionManager")
    @GetMapping("/transactions")
    public List<QuerySearchResult> findData(String query) {
        SearchSession searchSession = Search.session(canonicalManager);

        SearchResult<Transaction> result = searchSession.search(Transaction.class)
                .where(f -> f.simpleQueryString()
                        .field("accountIbanFrom")
                        .matching(query))
                .fetch(20);

        return result.hits().stream().map(it -> new QuerySearchResult(it)).toList();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuerySearchResult {
        private Transaction transaction;
    }
}
