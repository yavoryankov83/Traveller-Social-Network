package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.helper.Constants;
import com.telerikacademy.socialnetwork.models.LikedPost;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.LikedPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LikedPostServiceImpl implements com.telerikacademy.socialnetwork.services.contracts.LikedPostService {

  private LikedPostRepository likedPostRepository;

  @Autowired
  public LikedPostServiceImpl(LikedPostRepository likedPostRepository) {
    this.likedPostRepository = likedPostRepository;
  }

  @Override
  public LikedPost getLikedPostById(Integer likedPostId) {
    return likedPostRepository.findByIdAndEnabledTrue(likedPostId)
            .orElseThrow(() -> new NotFoundException(Constants.LIKED_POST_NOT_FOUND_MESSAGE));
  }

  @Override
  public LikedPost getLikedPostByUserAndPost(User user, Post post) {
    return likedPostRepository.findByUserAndPostAndEnabledTrue(user, post)
            .orElseThrow(() -> new NotFoundException(Constants.LIKED_POST_NOT_FOUND_MESSAGE));
  }

  @Override
  public List<LikedPost> getLikedPostByUser(User user) {
    return likedPostRepository.findAllByUserAndEnabledTrue(user);
  }

  @Override
  public List<LikedPost> getLikedPostByPost(Post post) {
    return likedPostRepository.findAllByPostAndEnabledTrue(post);
  }

  @Override
  public Integer getCountLikedPostByUser(User user) {
    return likedPostRepository.countAllByUserAndEnabledTrue(user);
  }

  @Override
  public Integer getCountLikedPostByPost(Post post) {
    return likedPostRepository.countAllByPostAndEnabledTrue(post);
  }

  @Override
  public boolean isLikeExist(Integer userId, Integer postId){
    return likedPostRepository.existsByUserIdAndPostIdAndEnabledTrue(userId, postId);
  }

  @Override
  @Transactional
  public void createLikedPost(LikedPost likedPost) {
    checkUserValidity(likedPost);
    checkPostValidity(likedPost);
    checkLikedPostIsExists(likedPost);

    LikedPost likedPostToCreate = new LikedPost();

    likedPostToCreate.setUser(likedPost.getUser());
    likedPostToCreate.setPost(likedPost.getPost());

    likedPostRepository.save(likedPostToCreate);
  }

  @Override
  @Transactional
  public void deleteLikedPost(Integer likedPostId) {
    LikedPost likedPostToDelete = getLikedPostById(likedPostId);

    likedPostToDelete.setEnabled(false);

    likedPostRepository.save(likedPostToDelete);
  }

  private void checkPostValidity(LikedPost likedPost) {
    if (likedPost.getPost() == null) {
      throw new BadRequestException(Constants.POST_NOT_VALID_MESSAGE);
    }
  }

  private void checkUserValidity(LikedPost likedPost) {
    if (likedPost.getUser() == null) {
      throw new BadRequestException(Constants.USER_NOT_VALID_MESSAGE);
    }
  }

  private void checkLikedPostIsExists(LikedPost likedPost) {
    if (likedPostRepository.existsByUserIdAndPostIdAndEnabledTrue(likedPost.getUser().getId(),
            likedPost.getPost().getId())) {
      throw new BadRequestException(Constants.LIKED_POST_ALREADY_EXISTS_MESSAGE);
    }
  }
}
