package org.example.hibernate_search_poc.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.canonical.Message;
import org.example.domain.canonical.Transaction;
import org.example.domain.master.Account;
import org.example.hibernate_search_poc.service.AccountsService;
import org.example.hibernate_search_poc.service.MessagesService;
import org.example.hibernate_search_poc.service.TransactionsService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final AccountsService accounts;
    private final MessagesService messages;
    private final TransactionsService transactions;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        var allAccounts = accounts.allAccounts();
        allAccounts.add(Account.newRandom());
        model.addAttribute("accounts", allAccounts);
        var allMessages = messages.allMessages();
        allMessages.add(Message.newRandom());
        model.addAttribute("messages", allMessages);
        return "index";
    }

    @PostMapping("/accounts")
    public String saveAccount(@ModelAttribute("account") Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/?q=" + bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).collect(Collectors.joining());
        }
        accounts.save(account);
        return "redirect:/";
    }

    @PostMapping("/accounts/{id}/delete")
    public String deleteAccount(@PathVariable long id) {
        accounts.delete(id);
        return "redirect:/";
    }

    @PostMapping("/messages")
    public String saveMessage(@ModelAttribute("message") Message message, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/?q=" + bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).collect(Collectors.joining());
        }
        messages.save(message);
        return "redirect:/";
    }

    @PostMapping("/messages/{id}/delete")
    public String deleteMessage(@PathVariable long id) {
        messages.delete(id);
        return "redirect:/";
    }

    @GetMapping ("/messages/{messageId}/transactions")
    public String viewTransactions(@PathVariable long messageId, Model model) {
        var message = messages.messageAndTransactionsById(messageId);
        message.getTransactions().add(Transaction.newRandom());
        model.addAttribute("message", message);
        return "transactions";
    }

    @PostMapping("/messages/{messageId}/transactions")
    public String saveTransaction(@PathVariable long messageId, @ModelAttribute("transaction") Transaction transaction, BindingResult bindingResult) {
        String redirectToTransactions = "redirect:/messages/" + messageId + "/transactions";
        if (bindingResult.hasErrors()) {
            return redirectToTransactions + "?q=" + bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).collect(Collectors.joining());
        }

        transactions.save(messageId, transaction);
        return redirectToTransactions;
    }

    @PostMapping("/messages/{messageId}/transactions/{id}/delete")
    public String deleteTransaction(@PathVariable long messageId, @PathVariable long id) {
        transactions.delete(id);
        return "redirect:/messages/" + messageId + "/transactions";
    }
}