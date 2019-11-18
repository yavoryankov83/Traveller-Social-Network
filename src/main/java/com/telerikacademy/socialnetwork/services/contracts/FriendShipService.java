package com.telerikacademy.socialnetwork.services.contracts;

import com.telerikacademy.socialnetwork.models.Friendship;
import com.telerikacademy.socialnetwork.models.User;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface FriendShipService {
  Friendship getFriendship(Integer userId, Integer friendId);

  Friendship getIfRelationExists(Integer userId, Integer friendId);

  boolean isUsersAreFriends(Principal principal, Integer friendId);

  boolean hasUsersRelation(Principal principal, Integer friendId);

  boolean isBlockedUserByPrincipal(Principal principal, Integer friendId);

  boolean hasPrincipalRequest(Integer userId, Principal principal);

  List<Friendship> getAllUserFriendShips(Integer userId);

  List<User> getAllUserFriends(Principal principal);

  List<User> getAllUserSentMeRequest(Principal principal);

  List<User> getUserFriends(Integer userId);

  Set<User> getAllNonRelation(Principal principal);

  void sendFriendShipRequest(Principal principal, Integer userId);

  void updateFriendShipRequest(Integer userId, Principal principal, Integer statusId);

  void acceptFriendshipRequest(Integer friendId, Integer principalId);

  void rejectFriendshipRequest(Integer friendId, Integer principalId);

  void blockFriendshipRequest(Integer friendId, Principal principal);

  void unblockFriendshipRequest(Principal principal, Integer friendId);
}
