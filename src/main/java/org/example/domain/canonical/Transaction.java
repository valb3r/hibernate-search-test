package org.example.domain.canonical;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.master.Account;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.iban4j.Iban;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
