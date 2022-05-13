package org.example.domain.canonical;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JsonIgnore
    private Message message;
}
