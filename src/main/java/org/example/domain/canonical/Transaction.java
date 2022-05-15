package org.example.domain.canonical;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.master.Account;
import org.example.hibernate_search_poc.config.SpringApplicationContext;
import org.example.hibernate_search_poc.service.AccountsService;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.iban4j.Iban;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@Entity
@Indexed
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @GenericField
    private BigDecimal amount;

    @FullTextField
    private String accountIbanFrom;

    @FullTextField
    private String accountIbanTo;

    @GenericField
    private LocalDateTime date;

    @FullTextField
    private String freeText;

    @ManyToOne
    @JsonIgnore
    private Message message;

    @Transient
    @IndexedEmbedded
    @IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "accountIbanFrom")))
    public Account getAccountFrom() {
        var accService = SpringApplicationContext.getBean(AccountsService.class);
        return accService.findByIban(accountIbanFrom);
    }

    @Transient
    @IndexedEmbedded
    @IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "accountIbanTo")))
    public Account getAccountTo() {
        var accService = SpringApplicationContext.getBean(AccountsService.class);
        return accService.findByIban(accountIbanTo);
    }

    public static Transaction newRandom() {
        var transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(ThreadLocalRandom.current().nextLong(0, 10000)).movePointLeft(2));
        transaction.setAccountIbanFrom(Iban.random().toString());
        transaction.setAccountIbanTo(Iban.random().toString());
        transaction.setDate(LocalDateTime.now());
        transaction.setFreeText(new Faker().commerce().productName());
        return transaction;
    }
}
