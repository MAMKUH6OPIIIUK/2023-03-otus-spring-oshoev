package ru.otus.spring.homework.oke.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.mapper.CommentMapper;
import ru.otus.spring.homework.oke.service.AuthorService;
import ru.otus.spring.homework.oke.service.BookService;
import ru.otus.spring.homework.oke.service.CommentService;
import ru.otus.spring.homework.oke.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    private final BookMapper bookMapper;

    private final CommentMapper commentMapper;

    @GetMapping("/")
    public String defaultRedirect() {
        return "redirect:/book";
    }

    @GetMapping("/book")
    public String listBookPage(Model model) {
        List<BookResponseDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book/{id}")
    public String bookInfoPage(@PathVariable Long id, Model model) {
        BookResponseDto book = this.bookService.findById(id);
        model.addAttribute("book", book);
        List<CommentResponseDto> comments = this.commentService.findByBookId(id);
        model.addAttribute("comments", comments);
        if (!model.containsAttribute("creatingComment")) {
            CommentRequestDto commentForCreate = new CommentRequestDto();
            model.addAttribute("creatingComment", commentForCreate);
        }
        return "book-info";
    }

    @GetMapping("/book/{id}/comment/edit/{commentId}")
    public String bookInfoEditCommentPage(@PathVariable(name = "id") Long id, @PathVariable(name = "commentId")
            Long commentId, Model model) {
        BookResponseDto book = this.bookService.findById(id);
        model.addAttribute("book", book);
        List<CommentResponseDto> comments = this.commentService.findByBookId(id);
        model.addAttribute("comments", comments);
        if (!model.containsAttribute("editingComment")) {
            CommentResponseDto editingComment = this.commentService.findById(commentId);
            CommentRequestDto commentForEdit = this.commentMapper.mapToCommentRequestDto(editingComment, id);
            model.addAttribute("editingComment", commentForEdit);
        }
        return "book-info-comment-edit";
    }

    @GetMapping("/book/create")
    public String createBookPage(Model model) {
        if (!model.containsAttribute("book")) {
            BookRequestDto bookForForm = new BookRequestDto();
            model.addAttribute("book", bookForForm);
        }
        List<AuthorFullNameDto> authors = this.authorService.findAll();
        model.addAttribute("authors", authors);
        List<GenreResponseDto> genres = this.genreService.findAll();
        model.addAttribute("genres", genres);
        return "edit-book";
    }

    @PostMapping("/book/create")
    public String createBook(@Valid @ModelAttribute("book") BookRequestDto book, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            redirectAttributes.addFlashAttribute("book", book);
            return "redirect:/book/create";
        }
        this.bookService.create(book);
        return "redirect:/book";
    }

    @GetMapping("/book/edit/{id}")
    public String editBookPage(@PathVariable(name = "id") Long id, Model model) {
        if (!model.containsAttribute("book")) {
            BookResponseDto editingBook = this.bookService.findById(id);
            BookRequestDto bookForForm = this.bookMapper.mapToBookRequestDto(editingBook);
            model.addAttribute("book", bookForForm);
        }
        List<AuthorFullNameDto> authors = this.authorService.findAll();
        model.addAttribute("authors", authors);
        List<GenreResponseDto> genres = this.genreService.findAll();
        model.addAttribute("genres", genres);
        return "edit-book";
    }

    @PostMapping("/book/edit")
    public String editBook(@Valid @ModelAttribute("book") BookRequestDto book, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            redirectAttributes.addFlashAttribute("book", book);
            return "redirect:/book/edit/" + book.getId();
        }
        this.bookService.update(book);
        return "redirect:/book";
    }

    @PostMapping("/book/delete/{id}")
    public String deleteBookById(@PathVariable Long id) {
        this.bookService.deleteById(id);
        return "redirect:/book";
    }
}
