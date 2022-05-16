package org.example.hibernate_search_poc.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.example.domain.canonical.Transaction;
import org.example.domain.master.Account;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.elasticsearch.search.projection.dsl.ElasticsearchSearchProjectionFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public List<QuerySearchResult> findData(String query) {
        var actualQuery = Strings.isBlank(query) ? "*" : query;

        SearchSession searchSession = Search.session(canonicalManager);

        SearchResult<Transaction> result = searchSession.search(Transaction.class)
                .where(f -> f.simpleQueryString()
                        .field("accountIbanFrom")
                        .field("accountIbanTo")
                        .field("accountFrom.name")
                        .field("accountTo.name")
                        .matching(actualQuery))
                .fetch(20);

        return result.hits().stream().map(it -> new QuerySearchResult(it, it.getAccountFrom(), it.getAccountTo())).toList();
    }

    public List<QuerySearchResult> findDataElasticOnly(String query) {
        var actualQuery = Strings.isBlank(query) ? "*" : query;

        SearchSession searchSession = Search.session(canonicalManager);

        // Also one can use GSON JsonObject and materialize out of it:
        var result = searchSession.search(Transaction.class)
                .extension(ElasticsearchExtension.get())
                .select(ElasticsearchSearchProjectionFactory::source)
                .where(f -> f.simpleQueryString()
                        .field("accountIbanFrom")
                        .field("accountIbanTo")
                        .field("accountFrom.name")
                        .field("accountTo.name")
                        .matching(actualQuery))
                .fetch(20);

        return result.hits().stream().map(QuerySearchResult::new).toList();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuerySearchResult {

        private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .create();

        private Transaction transaction;
        private Account accountFrom;
        private Account accountTo;

        public QuerySearchResult(JsonObject object) {
            this.transaction = GSON.fromJson(object, Transaction.class);
            this.accountFrom = GSON.fromJson(object.get("accountFrom"), Account.class);
            this.accountTo = GSON.fromJson(object.get("accountTo"), Account.class);
        }

        public String getAccountFromName() {
            if (null == accountFrom) {
                return null;
            }
            return accountFrom.getName();
        }

        public String getAccountToName() {
            if (null == accountTo) {
                return null;
            }
            return accountTo.getName();
        }

        private static class GsonLocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
                return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }

            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        }
    }
}
