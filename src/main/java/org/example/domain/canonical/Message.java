package org.example.domain.canonical;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @OneToMany(mappedBy = "message")
    private List<Transaction> transactions = new ArrayList<>();
}
