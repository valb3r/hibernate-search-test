package org.example.domain.canonical;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

@Getter
@Setter
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal amount;
    private String accountIbanFrom;
    private String accountIbanTo;
    private LocalDateTime date;

    @ManyToOne
    private Message message;
}
