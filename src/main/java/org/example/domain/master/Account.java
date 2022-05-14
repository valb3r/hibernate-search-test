package org.example.domain.master;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iban4j.Iban;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@Indexed
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
}
