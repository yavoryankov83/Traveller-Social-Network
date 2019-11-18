package com.telerikacademy.socialnetwork.services.contracts;

import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.LikedComment;
import com.telerikacademy.socialnetwork.models.User;

import javax.transaction.Transactional;
import java.util.List;

public interface LikedCommentService {

  LikedComment getLikedCommentById(Integer likedCommentId);

  LikedComment getLikedCommentByUserAndComment(User user, Comment comment);

  List<LikedComment> getLikedCommentsByUser(User user);

  List<LikedComment> getLikedCommentByComment(Comment comment);

  Integer getCountLikedCommentByUser(User user);

  Integer getCountLikedCommentByComment(Comment comment);

  boolean isLikeExist(Integer userId, Integer commentId);

  @Transactional
  void createLikedComment(LikedComment likedComment);

  @Transactional
  void deleteLikedComment(Integer likedCommentId);
}
