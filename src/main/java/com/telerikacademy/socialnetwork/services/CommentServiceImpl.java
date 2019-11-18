package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.helper.Constants;
import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.AbstractTimestampEntity;
import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements com.telerikacademy.socialnetwork.services.contracts.CommentService {

  private CommentRepository commentRepository;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Override
  public Comment getCommentById(Integer commentId) {
    return commentRepository.findByIdAndEnabledTrue(commentId)
            .orElseThrow(() -> new NotFoundException(Constants.COMMENT_NOT_FOUND_MESSAGE));
  }

  @Override
  public List<Comment> getAllCommentsByUserAndPost(User user, Post post) {
    return commentRepository.findAllByUserAndPostAndEnabledTrueOrderByUpdateDateDesc(user, post);
  }

  @Override
  public List<Comment> getAllCommentsByPost(Post post) {
    return commentRepository.findByPostAndEnabledTrueOrderByUpdateDateDesc(post);
  }

  @Override
  public List<Comment> getAllParentCommentsByPost(Post post) {
    return commentRepository.findByPostAndEnabledTrueAndParentCommentIsNullOrderByUpdateDateAsc(post);
  }

  @Override
  public List<Comment> getAllParentCommentsByPost(Post post, Integer pageNumber, Integer sizePerPage) {
    return commentRepository.findByPostAndEnabledTrueAndParentCommentIsNullOrderByUpdateDateDesc(post, Helper.createPageRequest(pageNumber, sizePerPage));
  }

  @Override
  public List<Comment> getAllChildCommentsByParentComment(Comment parentComment) {
    return parentComment.getComments()
            .stream()
            .filter(Comment::isEnabled)
            .sorted(Comparator.comparing(AbstractTimestampEntity::getUpdateDate))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void createComment(Comment comment) {
    checkUserValidity(comment);
    checkPostValidity(comment);
    checkContentValidity(comment);

    Comment commentToCreate = new Comment();

    commentToCreate.setContent(comment.getContent());
    commentToCreate.setPost(comment.getPost());
    commentToCreate.setUser(comment.getUser());

    if (comment.getParentComment() != null) {
      commentToCreate.setParentComment(comment.getParentComment());
    }

    commentRepository.save(commentToCreate);
  }

  @Override
  @Transactional
  public void updateComment(Integer commentId, Comment comment) {
    checkContentValidity(comment);

    Comment commentToUpdate = getCommentById(commentId);

    commentToUpdate.setContent(comment.getContent());

    commentRepository.save(commentToUpdate);
  }

  @Override
  @Transactional
  public void deleteComment(Integer commentId) {
    Comment commentToDelete = getCommentById(commentId);

    if (commentToDelete.getParentComment() == null) {
      List<Comment> childComments = commentRepository.findAllByParentCommentIdAndEnabledTrueOrderByUpdateDateDesc(commentId);
      childComments.forEach(comment -> {
        comment.setEnabled(false);
        commentRepository.save(comment);
      });
      commentToDelete.getPost().getComments().remove(commentToDelete);
    } else {
      commentToDelete.getParentComment().getComments().remove(commentToDelete);
    }

    commentToDelete.setEnabled(false);

    commentRepository.save(commentToDelete);
  }

  private void checkContentValidity(Comment comment) {
    if (comment.getContent() == null) {
      throw new BadRequestException(Constants.CONTENT_EMPTY_MESSAGE);
    }
  }

  private void checkPostValidity(Comment comment) {
    if (comment.getPost() == null) {
      throw new BadRequestException(Constants.POST_NOT_VALID_MESSAGE);
    }
  }

  private void checkUserValidity(Comment comment) {
    if (comment.getUser() == null) {
      throw new BadRequestException(Constants.USER_NOT_VALID_MESSAGE);
    }
  }
}
