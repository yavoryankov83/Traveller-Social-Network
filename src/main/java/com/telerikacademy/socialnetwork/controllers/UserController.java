package com.telerikacademy.socialnetwork.controllers;

import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.FriendShipService;
import com.telerikacademy.socialnetwork.services.contracts.PostService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {

  private UserService userService;
  private FriendShipService friendShipService;
  private PostService postService;

  @Autowired
  public UserController(UserService userService,
                        FriendShipService friendShipService,
                        PostService postService) {
    this.userService = userService;
    this.friendShipService = friendShipService;
    this.postService = postService;
  }

  @PostMapping(path = "register")
  public String postRegisterUserView(@RequestBody(required = false) MultipartFile photo,
                                     @RequestBody(required = false) MultipartFile cover,
                                     @Valid @ModelAttribute User user,
                                     BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      return "index";
    }
    checkIsUserPhotoExist(photo, user);
    checkIsCoverPhotoExist(cover, user);
    userService.createUser(user);
    return "login";
  }

  @GetMapping(path = "index")
  public String homeLandView(Model model, Principal principal) {
    Helper.checkPrincipal(principal);
    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    model.addAttribute("usersCount", userService.getAllUsers().size());
    model.addAttribute("postsCount", postService.getAllPublicPosts().size());

    return "index-land";
  }

  @GetMapping(path = "admin")
  public String adminLandView(Model model, Principal principal) {
    Helper.checkPrincipal(principal);
    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    model.addAttribute("users", userService.getAllUsers());

    return "admin";
  }

  @GetMapping(path = "timeline-about.html/{id}")
  public String getUserTimelineAboutView(Model model,
                                         Principal principal,
                                         @Valid @PathVariable(name = "id") Integer userId) {
    Helper.checkPrincipal(principal);
    User user = userService.getUserById(userId);
    User userPrincipal = userService.getPrincipal(principal);

    int friendsCount = friendShipService.getUserFriends(userId).size();
    int postsCount = postService.getPostsByUser(user).size();

    model.addAttribute("user", user);
    model.addAttribute("userPrincipal", userPrincipal);
    model.addAttribute("friendsCount", friendsCount);
    model.addAttribute("postsCount", postsCount);
    model.addAttribute("postToCreate", new Post());
    model.addAttribute("friends", friendShipService.getAllUserFriends(principal));
    model.addAttribute("areFriends", friendShipService.isUsersAreFriends(principal, userId));
    model.addAttribute("haveRelation", friendShipService.hasUsersRelation(principal, userId));
    model.addAttribute("haveRequest", friendShipService.hasPrincipalRequest(userId, principal));
    model.addAttribute("isBlocked", friendShipService.isBlockedUserByPrincipal(principal, userId));
    model.addAttribute("requests",friendShipService.getAllUserSentMeRequest(principal));

    return "timeline-about";
  }

  @GetMapping(path = "timeline-album.html")
  public String getPrincipalTimelineAlbumView(Model model, Principal principal) {
    Helper.checkPrincipal(principal);
    User user = userService.getPrincipal(principal);
    int friendsCount = friendShipService.getAllUserFriends(principal).size();

    model.addAttribute("userPrincipal", user);
    model.addAttribute("user", user);
    model.addAttribute("friendsCount", friendsCount);
    model.addAttribute("posts", postService.getPostsByUser(user));

    return "timeline-album";
  }

  @GetMapping(path = "timeline-album.html/{id}")
  public String getUserTimelineAlbumView(Model model,
                                         Principal principal,
                                         @Valid @PathVariable(name = "id") Integer userId) {

    Helper.checkPrincipal(principal);
    User user = userService.getUserById(userId);
    User userPrincipal = userService.getPrincipal(principal);

    int friendsCount = friendShipService.getUserFriends(userId).size();

    model.addAttribute("user", user);
    model.addAttribute("userPrincipal", userPrincipal);
    model.addAttribute("friendsCount", friendsCount);
    model.addAttribute("posts", postService.getPostsByUser(user));

    return "timeline-album";
  }

  @GetMapping(path = "timeline-update.html/{id}")
  public String getUpdateUserView(Model model,
                                  Principal principal,
                                  @Valid @PathVariable(name = "id") Integer userId) {

    Helper.checkPrincipal(principal);
    User user = userService.getUserById(userId);
    User userPrincipal = userService.getPrincipal(principal);

    int friendsCount = friendShipService.getAllUserFriends(principal).size();

    model.addAttribute("user", user);
    model.addAttribute("userPrincipal", userPrincipal);
    model.addAttribute("friendsCount", friendsCount);

    return "timeline-update";
  }

  @PostMapping(path = "timeline-update.html/{id}")
  public String postUpdateUserView(@RequestBody(required = false) MultipartFile photo,
                                   @RequestBody(required = false) MultipartFile cover,
                                   @Valid @PathVariable(name = "id") Integer userId,
                                   @Valid @ModelAttribute User user,
                                   BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "timeline-update";
    }
    checkIsUserPhotoExist(photo, user);
    checkIsCoverPhotoExist(cover, user);
    userService.updateUser(userId, user);

    return "redirect:/index";
  }

  @PostMapping(path = "timeline-about.html/delete/{id}")
  public String postDeleteUserView(@Valid @PathVariable(name = "id") Integer userId,
                                   @Valid @ModelAttribute User user,
                                   BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "index-land";
    }
    userService.deleteUser(userId);

    return "redirect:/";
  }

  private void checkIsCoverPhotoExist(@RequestBody(required = false) MultipartFile cover, @ModelAttribute @Valid User user) {
    if (cover != null) {
      try {
        if (!cover.isEmpty())
          user.setCoverPhoto(cover.getBytes());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void checkIsUserPhotoExist(@RequestBody(required = false) MultipartFile photo, @ModelAttribute @Valid User user) {
    if (photo != null) {
      try {
        if (!photo.isEmpty())
          user.setUserPhoto(photo.getBytes());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}