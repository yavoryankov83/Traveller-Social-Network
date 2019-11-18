package com.telerikacademy.socialnetwork.repositories;

import com.telerikacademy.socialnetwork.models.LikedPost;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikedPostRepository extends JpaRepository<LikedPost, Integer> {

  Optional<LikedPost> findByIdAndEnabledTrue(Integer likedPostId);

  Optional<LikedPost> findByUserAndPostAndEnabledTrue(User user, Post post);

  List<LikedPost> findAllByUserAndEnabledTrue(User user);

  List<LikedPost> findAllByPostAndEnabledTrue(Post post);

  Integer countAllByPostAndEnabledTrue(Post post);

  Integer countAllByUserAndEnabledTrue(User user);

  boolean existsByUserIdAndPostIdAndEnabledTrue(Integer userId, Integer postId);
}
