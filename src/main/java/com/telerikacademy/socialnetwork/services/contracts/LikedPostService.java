package com.telerikacademy.socialnetwork.services.contracts;

import com.telerikacademy.socialnetwork.models.LikedPost;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;

import javax.transaction.Transactional;
import java.util.List;

public interface LikedPostService {

  LikedPost getLikedPostById(Integer likedPostId);

  LikedPost getLikedPostByUserAndPost(User user, Post post);

  List<LikedPost> getLikedPostByUser(User user);

  List<LikedPost> getLikedPostByPost(Post post);

  Integer getCountLikedPostByUser(User user);

  Integer getCountLikedPostByPost(Post post);

  boolean isLikeExist(Integer userId, Integer postId);

  @Transactional
  void createLikedPost(LikedPost likedPost);

  @Transactional
  void deleteLikedPost(Integer likedPostId);
}
