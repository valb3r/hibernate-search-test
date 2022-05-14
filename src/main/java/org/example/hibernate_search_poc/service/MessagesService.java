package org.example.hibernate_search_poc.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.canonical.Message;
import org.example.domain.master.Account;
import org.example.domain.repository.canonical.MessagesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(transactionManager = "canonicalTransactionManager")
@RequiredArgsConstructor
public class MessagesService {

    private final MessagesRepository messages;

    public List<Message> allMessages() {
        return messages.findAll();
    }

    public void save(Message message) {
        messages.save(message);
    }

    public void delete(long id) {
        messages.deleteById(id);
    }
}
