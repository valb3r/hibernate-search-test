package org.example.domain.canonical;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    private UUID messageId;
    private String messageChannel;
    private Long sequenceNumber;

    @OneToMany(mappedBy = "message")
    private List<Transaction> transactions = new ArrayList<>();
}
