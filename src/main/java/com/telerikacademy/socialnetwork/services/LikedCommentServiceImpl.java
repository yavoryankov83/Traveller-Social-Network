package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.helper.Constants;
import com.telerikacademy.socialnetwork.models.*;
import com.telerikacademy.socialnetwork.repositories.LikedCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LikedCommentServiceImpl implements com.telerikacademy.socialnetwork.services.contracts.LikedCommentService {

  private LikedCommentRepository likedCommentRepository;

  @Autowired
  public LikedCommentServiceImpl(LikedCommentRepository likedCommentRepository) {
    this.likedCommentRepository = likedCommentRepository;
  }

  @Override
  public LikedComment getLikedCommentById(Integer likedCommentId) {
    return likedCommentRepository.findByIdAndEnabledTrue(likedCommentId)
            .orElseThrow(() -> new NotFoundException(Constants.LIKED_COMMENT_NOT_FOUND_MESSAGE));
  }

  @Override
  public LikedComment getLikedCommentByUserAndComment(User user, Comment comment) {
    return likedCommentRepository.findByUserAndCommentAndEnabledTrue(user, comment)
            .orElseThrow(() -> new NotFoundException(Constants.LIKED_COMMENT_NOT_FOUND_MESSAGE));
  }

  @Override
  public List<LikedComment> getLikedCommentsByUser(User user) {
    return likedCommentRepository.findAllByUserAndEnabledTrue(user);
  }

  @Override
  public List<LikedComment> getLikedCommentByComment(Comment comment) {
    return likedCommentRepository.findAllByCommentAndEnabledTrue(comment);
  }

  @Override
  public Integer getCountLikedCommentByUser(User user) {
    return likedCommentRepository.countAllByUserAndEnabledTrue(user);
  }

  @Override
  public Integer getCountLikedCommentByComment(Comment comment) {
    return likedCommentRepository.countAllByComment(comment);
  }

  @Override
  public boolean isLikeExist(Integer userId, Integer commentId){
    return likedCommentRepository.existsByUserIdAndCommentIdAndEnabledTrue(userId, commentId);
  }

  @Override
  @Transactional
  public void createLikedComment(LikedComment likedComment) {
    checkUserValidity(likedComment);
    checkCommentValidity(likedComment);
    checkLikedCommentIsExist(likedComment);

    LikedComment likedCommentToCreate = new LikedComment();

    likedCommentToCreate.setUser(likedComment.getUser());
    likedCommentToCreate.setComment(likedComment.getComment());

    likedCommentRepository.save(likedCommentToCreate);
  }

  @Override
  @Transactional
  public void deleteLikedComment(Integer likedCommentId) {
    LikedComment likedCommentToDelete = getLikedCommentById(likedCommentId);

    likedCommentToDelete.setEnabled(false);

    likedCommentRepository.save(likedCommentToDelete);
  }

  private void checkCommentValidity(LikedComment likedComment) {
    if (likedComment.getComment() == null) {
      throw new BadRequestException(Constants.COMMENT_NOT_VALID_MESSAGE);
    }
  }

  private void checkUserValidity(LikedComment likedComment) {
    if (likedComment.getUser() == null) {
      throw new BadRequestException(Constants.USER_NOT_VALID_MESSAGE);
    }
  }

  private void checkLikedCommentIsExist(LikedComment likedComment) {
    if (likedCommentRepository.existsByUserIdAndCommentIdAndEnabledTrue(likedComment.getUser().getId(),
            likedComment.getComment().getId())) {
      throw new BadRequestException(Constants.LIKED_COMMENT_ALREADY_EXISTS_MESSAGE);
    }
  }
}
