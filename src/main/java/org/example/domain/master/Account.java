package org.example.domain.master;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import org.example.hibernate_search_poc.config.SpringApplicationContext;
import org.example.hibernate_search_poc.service.TransactionsService;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iban4j.Iban;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostUpdate;

@Getter
@Setter
@Entity
@Indexed
@EntityListeners(Account.AccountSaveListener.class)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @FullTextField
    private String name;

    @FullTextField
    private String iban;

    public static Account newRandom() {
        var acc = new Account();
        acc.setIban(Iban.random().toString());
        acc.setName(new Faker().name().fullName() + " account");
        return acc;
    }

    public static class AccountSaveListener {

        @PostUpdate
        void onPostUpdate(Account account) {
            SpringApplicationContext.getBean(TransactionsService.class).updateAccount(account);
        }
    }
}
