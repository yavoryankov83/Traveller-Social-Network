package com.telerikacademy.socialnetwork.controllers;

import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.services.contracts.CommentService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping
public class CommentController {

  private CommentService commentService;
  private UserService userService;

  @Autowired
  public CommentController(CommentService commentService, UserService userService) {
    this.commentService = commentService;
    this.userService = userService;
  }

  @GetMapping(path = "comments/{id}")
  public String getCommentView(@Valid @PathVariable(name = "id") Integer commentId, Model model) {
    model.addAttribute("comment", commentService.getCommentById(commentId));

    return "redirect:/newsfeed.html";
  }

  @GetMapping(path = "comments/update/{id}")
  public String getUpdateCommentView(@Valid @PathVariable(name = "id") Integer commentId,
                                  Model model,
                                  Principal principal) {
    Helper.checkPrincipal(principal);
    model.addAttribute("commentToUpdate", commentService.getCommentById(commentId));
    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    return "redirect:/newsfeed.html";
  }

  @PostMapping(path = "comments/update/{id}")
  public String getUpdateCommentView(@Valid @PathVariable(name = "id") Integer commentId,
                                   @Valid @ModelAttribute(name = "commentToUpdate") Comment commentToUpdate,
                                   BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
      return "newsfeed.html";
    }
    commentService.updateComment(commentId, commentToUpdate);

    return "redirect:/newsfeed.html";
  }
}
