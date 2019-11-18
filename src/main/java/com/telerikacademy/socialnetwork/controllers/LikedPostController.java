package com.telerikacademy.socialnetwork.controllers;

import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.LikedPost;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.LikedPostService;
import com.telerikacademy.socialnetwork.services.contracts.PostService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(path = "newsfeed.html")
public class LikedPostController {

  private LikedPostService likedPostService;
  private PostService postService;
  private UserService userService;

  @Autowired
  public LikedPostController(LikedPostService likedPostService,
                             PostService postService,
                             UserService userService) {
    this.likedPostService = likedPostService;
    this.postService = postService;
    this.userService = userService;
  }

  @GetMapping(path = "{id}/likedPosts")
  public String getPostLikes(Model model, @Valid @PathVariable(name = "id") Integer postId, Principal principal) {
    Helper.checkPrincipal(principal);
    Post post = postService.getPostById(postId);
    model.addAttribute("userPrincipal",userService.getPrincipal(principal));
    model.addAttribute("numberLikes", likedPostService.getCountLikedPostByPost(post));
    model.addAttribute("likedPost", new LikedPost());

    return "newsfeed";
  }

  @PostMapping(path = "{id}/likedPosts")
  public String createPostLikes(@Valid @ModelAttribute(name = "likedPost") LikedPost likedPost,
                                @Valid @ModelAttribute(name = "userPrincipal") User userPrincipal,
                                @Valid @PathVariable(name = "id") Integer postId,
                                BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "newsfeed";
    }

    likedPost.setPost(postService.getPostById(postId));
    likedPost.setUser(userPrincipal);
    likedPostService.createLikedPost(likedPost);

    return "newsfeed";
  }
}
