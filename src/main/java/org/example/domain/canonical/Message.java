package org.example.domain.canonical;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.master.Account;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.iban4j.Iban;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@Entity
@Indexed
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @GenericField
    private UUID messageId;

    @FullTextField
    private String messageChannel;

    @GenericField
    private Long sequenceNumber;

    @IndexedEmbedded
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public void addTransaction(Transaction transaction) {
        if (null == transactions) {
            transactions = new ArrayList<>();
        }

        transaction.setMessage(this);
        transactions.add(transaction);
    }

    public static Message newRandom() {
        var msg = new Message();
        msg.setMessageId(UUID.randomUUID());
        msg.setMessageChannel(new Faker().hacker().abbreviation() + " " + new Faker().internet().domainWord());
        msg.setSequenceNumber(ThreadLocalRandom.current().nextLong(0, 100000));
        return msg;
    }
}
