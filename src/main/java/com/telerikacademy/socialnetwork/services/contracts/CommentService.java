package com.telerikacademy.socialnetwork.services.contracts;

import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentService {

  Comment getCommentById(Integer commentId);

  List<Comment> getAllCommentsByUserAndPost(User user, Post post);

  List<Comment> getAllCommentsByPost(Post post);

  List<Comment> getAllParentCommentsByPost(Post post);

  List<Comment> getAllParentCommentsByPost(Post post, Integer pageNumber, Integer sizePerPage);

  List<Comment> getAllChildCommentsByParentComment(Comment parentComment);

  @Transactional
  void createComment(Comment comment);

  @Transactional
  void updateComment(Integer commentId, Comment comment);

  @Transactional
  void deleteComment(Integer commentId);
}
