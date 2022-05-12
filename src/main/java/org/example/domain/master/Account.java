package org.example.domain.master;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

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

    private String name;

    private String iban;
}
