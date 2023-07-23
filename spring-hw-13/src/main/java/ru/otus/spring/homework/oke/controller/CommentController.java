package ru.otus.spring.homework.oke.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/create")
    public String createComment(@Valid @ModelAttribute("creatingComment") CommentRequestDto comment,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "creatingComment",
                    bindingResult);
            redirectAttributes.addFlashAttribute("creatingComment", comment);
            Long pageBookId = comment.getBookId();
            return "redirect:/book/" + pageBookId;
        }
        this.commentService.create(comment);
        Long pageBookId = comment.getBookId();
        return "redirect:/book/" + pageBookId;
    }

    @PostMapping("/comment/edit")
    public String editComment(@Valid @ModelAttribute("editingComment") CommentRequestDto comment,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "editingComment",
                    bindingResult);
            redirectAttributes.addFlashAttribute("editingComment", comment);
            Long pageBookId = comment.getBookId();
            Long commentId = comment.getId();
            return "redirect:/book/" + pageBookId + "/comment/edit/" + commentId;
        }
        this.commentService.update(comment);
        Long pageBookId = comment.getBookId();
        return "redirect:/book/" + pageBookId;
    }

    @PostMapping("/comment/delete")
    public String deleteComment(@ModelAttribute("deletingComment") CommentRequestDto comment,
                                BindingResult bindingResult) {
        Long commentId = comment.getId();
        this.commentService.deleteById(commentId);
        Long pageBookId = comment.getBookId();
        return "redirect:/book/" + pageBookId;
    }
}
