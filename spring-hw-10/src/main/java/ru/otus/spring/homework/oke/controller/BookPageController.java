package ru.otus.spring.homework.oke.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookPageController {
    @GetMapping("/")
    public String defaultRedirect() {
        return "redirect:/book";
    }

    @GetMapping("/book")
    public String listBookPage(Model model) {
        return "books";
    }

    @GetMapping("/book/{id}")
    public String bookInfoPage(@PathVariable Long id, Model model) {
        model.addAttribute("bookId", id);
        return "book-info";
    }

    @GetMapping("/book/create")
    public String createBookPage(Model model) {
        return "edit-book";
    }

    @GetMapping("/book/edit/{id}")
    public String editBookPage(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("bookId", id);
        return "edit-book";
    }
}
