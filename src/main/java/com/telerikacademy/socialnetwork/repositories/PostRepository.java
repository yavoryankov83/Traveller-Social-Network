package com.telerikacademy.socialnetwork.repositories;

import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  List<Post> findAllByEnabledTrueOrderByUpdateDateDesc();

  List<Post> findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc();

  List<Post> findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc(Pageable pageable);

  Optional<Post> findByIdAndEnabledTrue(Integer postId);

  List<Post> findAllByUserAndEnabledTrueOrderByUpdateDateDesc(User user);

  List<Post> findAllByUserAndEnabledTrueOrderByUpdateDateDesc(User user, Pageable pageable);

  List<Post> findAllByUserAndEnabledTrueAndVisibleFalse(User user);

  @Query(value = "(select * from social_network.posts p\n" +
          "join social_network.users u on p.user_id = u.id\n" +
          "join social_network.friendships f on u.id = f.friend_id\n" +
          "where (f.user_id = ?1 and f.friendship_status_id = 2\n" +
          "and p.visibility = false and f.enabled is true\n" +
          "and p.enabled is true and u.enabled is true));", nativeQuery = true)
  List<Post> findAllPrivatePostsOfFriendShipsRequestedByPrincipal(Integer principalId);

  @Query(value = "(select * from social_network.posts p\n" +
          "join social_network.users u on p.user_id = u.id\n" +
          "join social_network.friendships f on u.id = f.user_id\n" +
          "where (f.friend_id = ?1 and f.friendship_status_id = 2\n" +
          "and p.visibility = false and f.enabled is true\n" +
          "and p.enabled is true and u.enabled is true));", nativeQuery = true)
  List<Post> findAllPrivatePostsOfFriendShipsAcceptedByPrincipal(Integer principalId);

  boolean existsByContentAndEnabledTrueAndIdNotLike(String content, Integer postId);
}
