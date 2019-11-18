package com.telerikacademy.socialnetwork.controllers;

import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.services.contracts.FriendShipService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(path = "friends")
public class FriendShipController {

  private FriendShipService friendShipService;
  private UserService userService;

  @Autowired
  public FriendShipController(FriendShipService friendShipService,
                              UserService userService) {
    this.friendShipService = friendShipService;
    this.userService = userService;
  }

  @GetMapping
  public String getFriendsView(Model model, Principal principal) {
    Helper.checkPrincipal(principal);
    model.addAttribute("userPrincipal", userService.getPrincipal(principal));
    model.addAttribute("friends", friendShipService.getAllUserFriends(principal));
    model.addAttribute("friendsCount", friendShipService.getAllUserFriends(principal).size());
    model.addAttribute("nonFriends", friendShipService.getAllNonRelation(principal));
    model.addAttribute("newPost", new Post());

    return "newsfeed-friends";
  }

  @GetMapping(path = "{id}")
  public String getFriendView(Model model, @Valid @PathVariable(name = "id") Integer friendId, Principal principal) {
    Helper.checkPrincipal(principal);

    if (friendShipService.isUsersAreFriends(principal, friendId)) {
      model.addAttribute("friend", userService.getUserById(friendId));
    }

    return "user-profile";
  }
}
