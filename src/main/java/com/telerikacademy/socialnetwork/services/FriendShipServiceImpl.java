package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.helper.Constants;
import com.telerikacademy.socialnetwork.models.Friendship;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.FriendShipRepository;
import com.telerikacademy.socialnetwork.repositories.UserRepository;
import com.telerikacademy.socialnetwork.services.contracts.FriendShipService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendShipServiceImpl implements FriendShipService {

  private FriendShipRepository friendShipRepository;
  private UserRepository userRepository;

  public FriendShipServiceImpl(FriendShipRepository friendShipRepository,
                               UserRepository userRepository) {
    this.friendShipRepository = friendShipRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Friendship getFriendship(Integer userId, Integer friendId) {
    return friendShipRepository.findIfUsersAreFriends(userId, friendId)
            .orElseThrow(() -> new NotFoundException(Constants.NO_FRIENDSHIP_MESSAGE));
  }

  @Override
  public Friendship getIfRelationExists(Integer userId, Integer friendId) {
    return friendShipRepository.checkIfRelationExistsBetweenUsers(userId, friendId)
            .orElseThrow(() -> new NotFoundException(Constants.NO_FRIENDSHIP_MESSAGE));
  }

  @Override
  public boolean isUsersAreFriends(Principal principal, Integer friendId) {
    User userPrincipal = getPrincipal(principal);
    if (userPrincipal.getId().equals(friendId)) {
      return false;
    }
    return (friendShipRepository.findIfUsersAreFriends(userPrincipal.getId(), friendId).isPresent());
  }

  @Override
  public boolean hasUsersRelation(Principal principal, Integer friendId) {
    User userPrincipal = getPrincipal(principal);
    if (userPrincipal.getId().equals(friendId)) {
      return false;
    }
    return (friendShipRepository.checkIfRelationExistsBetweenUsers(userPrincipal.getId(), friendId).isPresent());
  }

  @Override
  public boolean isBlockedUserByPrincipal(Principal principal, Integer friendId) {
    User userPrincipal = getPrincipal(principal);
    if (userPrincipal.getId().equals(friendId)) {
      return false;
    }
    if (hasUsersRelation(principal, friendId)) {
      Friendship relationship = getIfRelationExists(userPrincipal.getId(), friendId);
      if (relationship.getStatusId().equals(Constants.FRIENDSHIP_BLOCKED_STATUS)
              && relationship.getUser().getId().equals(userPrincipal.getId())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean hasPrincipalRequest(Integer userId, Principal principal) {
    User userPrincipal = getPrincipal(principal);
    if (userPrincipal.getId().equals(userId)) {
      return false;
    }
    return (friendShipRepository.findRequestSendByUser(userId, userPrincipal.getId()).isPresent());
  }

  @Override
  public List<Friendship> getAllUserFriendShips(Integer userId) {
    return friendShipRepository.findAllUserFriendShips(userId);
  }

  @Override
  public List<User> getAllUserFriends(Principal principal) {
    User userPrincipal = getPrincipal(principal);

    List<Integer> listFriendsId = friendShipRepository.findAllFriendsId(userPrincipal.getId());
    listFriendsId.addAll(friendShipRepository.findAllUsersId(userPrincipal.getId()));

    return listFriendsId.stream()
            .map(id -> userRepository.findByIdAndEnabledTrue(id).get())
            .collect(Collectors.toList());
  }

  @Override
  public List<User> getAllUserSentMeRequest(Principal principal) {
    User userPrincipal = getPrincipal(principal);

    List<Integer> requestingUsersId = friendShipRepository.findRequestToPrincipal(userPrincipal.getId());

    return requestingUsersId.stream()
            .map(id -> userRepository.findByIdAndEnabledTrue(id).get())
            .collect(Collectors.toList());
  }

  @Override
  public List<User> getUserFriends(Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (!user.isPresent()) {
      throw new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE);
    }

    User currentUser = user.get();

    List<Integer> listFriendsId = friendShipRepository.findAllFriendsId(currentUser.getId());
    listFriendsId.addAll(friendShipRepository.findAllUsersId(currentUser.getId()));

    return listFriendsId.stream()
            .map(id -> userRepository.findByIdAndEnabledTrue(id).get())
            .collect(Collectors.toList());
  }

  @Override
  public Set<User> getAllNonRelation(Principal principal) {
    User userPrincipal = getPrincipal(principal);
    List<User> allUsersNonRelation = userRepository.findAllByEnabledTrueOrderByUpdateDateDesc();

    Set<User> nonFriends = new HashSet<>();
    for (User user : allUsersNonRelation) {
      if (!userPrincipal.getId().equals(user.getId()) && !hasUsersRelation(principal, user.getId())&&nonFriends.size()<=9) {
        nonFriends.add(user);
      }
    }
    return nonFriends;
  }

  @Override
  public void sendFriendShipRequest(Principal principal, Integer userId) {
    User userPrincipal = getPrincipal(principal);

    if (friendShipRepository.checkIfRelationExistsBetweenUsers(userPrincipal.getId(), userId).isPresent()) {
      throw new NotFoundException(Constants.FRIENDSHIP_EXISTS_MESSAGE);
    }

    Friendship friendship = new Friendship();

    friendship.setUser(userPrincipal);
    Optional<User> user = userRepository.findById(userId);
    user.ifPresent(friendship::setFriend);

    friendShipRepository.save(friendship);
  }

  @Override
  public void updateFriendShipRequest(Integer userId, Principal principal, Integer statusId) {
    User userPrincipal = getPrincipal(principal);

    if (!friendShipRepository.checkIfRelationExistsBetweenUsers(userPrincipal.getId(), userId).isPresent()) {
      throw new NotFoundException(Constants.NO_FRIENDSHIP_MESSAGE);
    }

    Optional<Friendship> friendShipSendToPrincipal = friendShipRepository
            .getFriendShipWherePrincipalIsFriend(userId, userPrincipal.getId());

    if (friendShipSendToPrincipal.isPresent()) {
      Friendship friendship = friendShipSendToPrincipal.get();
      friendship.setStatusId(statusId);

      friendShipRepository.save(friendship);
    }
  }

  @Override
  public void acceptFriendshipRequest(Integer friendId, Integer principalId) {
    friendShipRepository.acceptFriendShipRequest(friendId, principalId);
  }

  @Override
  public void rejectFriendshipRequest(Integer friendId, Integer principalId) {
    friendShipRepository.rejectFriendShipRequest(friendId, principalId);
  }

  @Override
  public void blockFriendshipRequest(Integer friendId, Principal principal) {
    User userPrincipal = getPrincipal(principal);
    if (!isUsersAreFriends(principal, friendId)) {
      throw new NotFoundException(Constants.NO_FRIENDSHIP_MESSAGE);
    }
    Friendship friendship = getFriendship(friendId, userPrincipal.getId());
    if (friendship.getUser().getId().equals(friendId)) {
      friendship.setUser(userPrincipal);
      friendship.setFriend(userRepository.findById(friendId).get());
    }
    friendship.setStatusId(Constants.FRIENDSHIP_BLOCKED_STATUS);
    friendShipRepository.save(friendship);
  }

  @Override
  public void unblockFriendshipRequest(Principal principal, Integer friendId) {
    User userPrincipal = getPrincipal(principal);
    Friendship friendship = getIfRelationExists(userPrincipal.getId(), friendId);
    if (friendship.getStatusId().equals(Constants.FRIENDSHIP_BLOCKED_STATUS)) {
      friendship.setStatusId(Constants.FRIENDSHIP_ACCEPTED_STATUS);
      friendShipRepository.save(friendship);
    }
  }

  private User getPrincipal(Principal principal) {
    String principalName = principal.getName();

    Optional<User> currentPrincipal = userRepository.findByUsernameAndEnabledTrue(principalName);
    if (!currentPrincipal.isPresent()) {
      throw new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE);
    }
    return currentPrincipal.get();
  }
}
