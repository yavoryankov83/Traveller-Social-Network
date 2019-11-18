package com.telerikacademy.socialnetwork.repositories;

import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

  Optional<Comment> findByIdAndEnabledTrue(Integer commentId);

  List<Comment> findByPostAndEnabledTrueOrderByUpdateDateDesc(Post post);

  List<Comment> findByPostAndEnabledTrueAndParentCommentIsNullOrderByUpdateDateAsc(Post post);

  List<Comment> findByPostAndEnabledTrueAndParentCommentIsNullOrderByUpdateDateDesc(Post post, Pageable pageable);

  List<Comment> findAllByUserAndEnabledTrueOrderByUpdateDateDesc(User user);

  List<Comment> findAllByUserAndPostAndEnabledTrueOrderByUpdateDateDesc(User user, Post post);

  List<Comment> findAllByParentCommentIdAndEnabledTrueOrderByUpdateDateDesc(Integer parenCommentId);
}
