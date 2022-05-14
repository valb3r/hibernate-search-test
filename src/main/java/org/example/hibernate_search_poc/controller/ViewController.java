package org.example.hibernate_search_poc.controller;

import liquibase.pro.packaged.A;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.domain.canonical.Message;
import org.example.domain.master.Account;
import org.example.hibernate_search_poc.service.AccountsService;
import org.example.hibernate_search_poc.service.MessagesService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final AccountsService accounts;
    private final MessagesService messages;

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
}