package com.telerikacademy.socialnetwork.controllers;

import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.PostService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private UserService userService;
  private PostService postService;

  public HomeController(UserService userService,
                        PostService postService) {
    this.userService = userService;
    this.postService = postService;
  }

  @GetMapping(path = "/")
  public String homeView(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("users", userService.getAllUsers());
    model.addAttribute("usersCount", userService.getAllUsers().size());
    model.addAttribute("postsCount", postService.getAllPublicPosts().size());

    return "index";
  }

  @GetMapping(path = "login")
  public String loginView(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("usersCount", userService.getAllUsers().size());
    model.addAttribute("postsCount", postService.getAllPublicPosts().size());

    return "login";
  }

  @GetMapping("access-denied")
  public String accessDeniedView() {
    return "error-401";
  }
}