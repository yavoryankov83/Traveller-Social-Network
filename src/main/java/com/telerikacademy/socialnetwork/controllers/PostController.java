package com.telerikacademy.socialnetwork.controllers;

import com.telerikacademy.socialnetwork.helper.Constants;
import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping
public class PostController {

  private PostService postService;
  private CommentService commentService;
  private UserService userService;
  private FriendShipService friendShipService;
  private LikedPostService likedPostService;


  @Autowired
  public PostController(PostService postService,
                        CommentService commentService,
                        UserService userService,
                        FriendShipService friendShipService,
                        LikedPostService likedPostService) {
    this.postService = postService;
    this.commentService = commentService;
    this.userService = userService;
    this.friendShipService = friendShipService;
    this.likedPostService = likedPostService;
  }

  private static Integer count = Constants.POSTS_INITIAL_SIZE_PER_PAGE;

  @GetMapping(path = "newsfeed.html")
  public String getPostsView(Model model, Principal principal) {
    Helper.checkPrincipal(principal);

    List<Post> result;

    List<Post> list = postService.getAllPostsVisibleByPrincipal(principal);
    if (list.size() > 3){
      result = postService.getAllPostsVisibleByPrincipal(principal, Constants.POSTS_INITIAL_SIZE_PER_PAGE);
    } else {
      result = postService.getAllPostsVisibleByPrincipal(principal);
    }

    count += 3;

    model.addAttribute("currentCount", postService.getAllPostsVisibleByPrincipal(principal).size());
    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    model.addAttribute("posts", result);
    model.addAttribute("friends", friendShipService.getAllUserFriends(principal));
    model.addAttribute("friendsCount", friendShipService.getAllUserFriends(principal).size());
    model.addAttribute("newPost", new Post());
    model.addAttribute("newComment", new Comment());
    model.addAttribute("nonFriends", friendShipService.getAllNonRelation(principal));

    return "newsfeed";
  }

  @GetMapping(path = "newsfeed.html/pageable")
  public String getPostsViewPageable(Model model, Principal principal) {
    int maxSize = postService.getAllPostsVisibleByPrincipal(principal).size();

    model.addAttribute("maxSize", maxSize);
    model.addAttribute("count", count);

    Helper.checkPrincipal(principal);
    if (count > maxSize) {
      count = maxSize;
    }
    List<Post> allPostsVisibleByPrincipal = postService.getAllPostsVisibleByPrincipal(principal, count);
    count += 3;

    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    model.addAttribute("posts", allPostsVisibleByPrincipal);
    model.addAttribute("friends", friendShipService.getAllUserFriends(principal));
    model.addAttribute("friendsCount", friendShipService.getAllUserFriends(principal).size());
    model.addAttribute("newPost", new Post());
    model.addAttribute("newComment", new Comment());
    model.addAttribute("nonFriends", friendShipService.getAllNonRelation(principal));
    model.addAttribute("contentComment", "");

    return "newsfeed";
  }

  @GetMapping(path = "newsfeed.html/{id}")
  public String getPostView(Model model, Principal principal,
                            @Valid @PathVariable(name = "id") Integer postId) {
    Helper.checkPrincipal(principal);
    Post post = postService.getPostById(postId);
    model.addAttribute("post", post);
    model.addAttribute("numberLikes", likedPostService.getCountLikedPostByPost(post));
    model.addAttribute("comments", commentService.getAllCommentsByPost(post));

    return "newsfeed";
  }

  @PostMapping(path = "newsfeed.html/create")
  public String posCreatePostView(@RequestBody(required = false) MultipartFile photo,
                                  Model model,
                                  Principal principal,
                                  @Valid @ModelAttribute(name = "newPost") Post post,
                                  BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "newsfeed";
    }
    checkIsPhotoExist(photo, post);
    User userPrincipal = userService.getPrincipal(principal);
    model.addAttribute("userPrincipal", userPrincipal);
    post.setUser(userPrincipal);
    postService.createPost(post);

    return "redirect:/newsfeed.html";
  }

  @GetMapping(path = "newsfeed-images.html")
  public String getImagePostsView(Model model, Principal principal) {
    Helper.checkPrincipal(principal);
    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    model.addAttribute("posts", postService.getAllPostsVisibleByPrincipal(principal));
    model.addAttribute("friends", friendShipService.getAllUserFriends(principal));
    model.addAttribute("countFriends", friendShipService.getAllUserFriends(principal).size());
    model.addAttribute("newPost", new Post());
    model.addAttribute("nonFriends", friendShipService.getAllNonRelation(principal));

    return "newsfeed-images";
  }

  @GetMapping(path = "newsfeed.html/update/{id}")
  public String getUpdatePostView(@Valid @PathVariable(name = "id") Integer postId,
                                  Model model,
                                  Principal principal) {
    Helper.checkPrincipal(principal);
    model.addAttribute("postToUpdate", postService.getPostById(postId));
    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    return "newsfeed";
  }

  @PostMapping(path = "newsfeed.html/update/{id}")
  public String postUpdatePostView(@RequestBody(required = false) MultipartFile photo,
                                   Model model,
                                   @Valid @PathVariable(name = "id") Integer postId,
                                   @Valid @ModelAttribute User user,
                                   BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "newsfeed";
    }
    Post post = postService.getPostById(postId);
    model.addAttribute("postToUpdate", post);
    checkIsPhotoExist(photo, post);
    postService.updatePost(postId, post);

    return "index-land";
  }

  @GetMapping(path = "newsfeed.html/delete/{id}")
  public String getDeletePostView(@Valid @PathVariable(name = "id") Integer postId,
                                  Model model,
                                  Principal principal) {
    Helper.checkPrincipal(principal);
    model.addAttribute("post", postService.getPostById(postId));
    String principalName = principal.getName();
    User currentPrincipal = userService.getUserByUsername(principalName);
    model.addAttribute("user", currentPrincipal);

    return "index-land";
  }

  @PostMapping(path = "newsfeed.html/delete/{id}")
  public String postDeletePostView(@Valid @PathVariable(name = "id") Integer postId,
                                   @Valid @ModelAttribute Post post,
                                   @Valid @ModelAttribute User user,
                                   BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "index-land";
    }
    postService.deletePost(postId);

    return "index-land";
  }

  private void checkIsPhotoExist(MultipartFile photo, Post post) {
    if (photo != null) {
      try {
        if (!photo.isEmpty())
          post.setPostPhoto(photo.getBytes());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
