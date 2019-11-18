package com.telerikacademy.socialnetwork.repositories;

import com.telerikacademy.socialnetwork.models.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<Friendship, Integer> {

  @Query(value = "select * from friendships f\n" +
          "where (f.user_id = ?1 or f.friend_id = ?1)\n" +
          "and (f.user_id = ?2 or f.friend_id = ?2)\n" +
          "and f.enabled = true;", nativeQuery = true)
  Optional<Friendship> checkIfRelationExistsBetweenUsers(Integer principalId, Integer friendId);

  @Query(value = "select * from friendships f\n" +
          "where (f.user_id = ?1 or f.friend_id = ?1)\n" +
          "and (f.user_id = ?2 or f.friend_id = ?2)\n" +
          "and f.enabled = true\n" +
          "and f.friendship_status_id = 2;", nativeQuery = true)
  Optional<Friendship> findIfUsersAreFriends(Integer principalId, Integer friendId);

  @Query(value = "select * from friendships f\n" +
          "where (user_id = ?1 or friend_id = ?1)\n" +
          "and f.enabled is true\n" +
          "and f.friendship_status_id = 2", nativeQuery = true)
  List<Friendship> findAllUserFriendShips(Integer principalId);

  @Query(value = "select f.user_id from friendships f\n" +
          "where friend_id = ?1\n" +
          "and f.enabled is true\n" +
          "and f.friendship_status_id = 1", nativeQuery = true)
  List<Integer> findRequestToPrincipal(Integer principalId);

  @Query(value = "select f.friend_id from friendships f\n" +
          "where user_id = ?1\n" +
          "and f.enabled is true\n" +
          "and f.friendship_status_id = 2", nativeQuery = true)
  List<Integer> findAllFriendsId(Integer principalId);

  @Query(value = "select f.user_id from friendships f\n" +
          "where friend_id = ?1\n" +
          "and f.enabled is true\n" +
          "and f.friendship_status_id = 2", nativeQuery = true)
  List<Integer> findAllUsersId(Integer principalId);

  @Query(value = "select * from friendships f\n" +
          "where (f.user_id = ?1 and f.friend_id = ?2)\n" +
          "and f.enabled = true;", nativeQuery = true)
  Optional<Friendship> getFriendShipWherePrincipalIsFriend(Integer userId, Integer principleId);

  @Query(value = "select * from friendships\n" +
          "where (user_id = ?1 and friend_id = ?2)\n" +
          "and friendship_status_id = 1\n" +
          "and enabled = true", nativeQuery = true)
  Optional<Friendship> findRequestSendByUser(Integer userId, Integer principleId);

  @Transactional
  @Modifying
  @Query(value = "update friendships\n" +
          "set friendship_status_id = 2\n" +
          "where user_id = ?1\n" +
          "and friend_id = ?2\n" +
          "and enabled = true", nativeQuery = true)
  void acceptFriendShipRequest(Integer friendId, Integer principalId);

  @Transactional
  @Modifying
  @Query(value = "update friendships\n" +
          "set friendship_status_id = 3\n" +
          "where user_id = ?1\n" +
          "and friend_id = ?2\n" +
          "and enabled = true", nativeQuery = true)
  void rejectFriendShipRequest(Integer friendId, Integer principalId);
}