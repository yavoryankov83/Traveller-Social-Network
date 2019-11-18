package com.telerikacademy.socialnetwork.repositories;

import com.telerikacademy.socialnetwork.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikedCommentRepository extends JpaRepository<LikedComment, Integer> {

  Optional<LikedComment> findByIdAndEnabledTrue(Integer likedCommentId);

  Optional<LikedComment> findByUserAndCommentAndEnabledTrue(User user, Comment comment);

  List<LikedComment> findAllByUserAndEnabledTrue(User user);

  List<LikedComment> findAllByCommentAndEnabledTrue(Comment comment);

  Integer countAllByUserAndEnabledTrue(User user);

  Integer countAllByComment(Comment comment);

  boolean existsByUserIdAndCommentIdAndEnabledTrue(Integer userId, Integer commentId);
}
