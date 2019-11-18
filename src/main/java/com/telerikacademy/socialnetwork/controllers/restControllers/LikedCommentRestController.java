package com.telerikacademy.socialnetwork.controllers.restControllers;

import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.LikedComment;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.CommentService;
import com.telerikacademy.socialnetwork.services.contracts.LikedCommentService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Api(value = "Social Network Web Application")
@RestController
@RequestMapping(path = "api/v1/likedComments")
public class LikedCommentRestController {

  private LikedCommentService likedCommentService;
  private UserService userService;
  private CommentService commentService;

  @Autowired
  public LikedCommentRestController(LikedCommentService likedCommentService,
                                    UserService userService,
                                    CommentService commentService) {
    this.likedCommentService = likedCommentService;
    this.userService = userService;
    this.commentService = commentService;
  }

  @ApiOperation(value = "Get liked comment by ID", response = LikedComment.class)
  @GetMapping(path = "{id}")
  public LikedComment getById(@ApiParam(value = "Liked comment id from which liked comment object will retrieve", required = true)
                              @Valid @PathVariable(name = "id") Integer likedCommentId) {
    return likedCommentService.getLikedCommentById(likedCommentId);
  }

  @ApiOperation(value = "Get liked comment by user and by comment", response = LikedComment.class)
  @GetMapping(path = "users/{userId}/comments/{commentId}")
  public LikedComment getByUserAndComment(@ApiParam(value = "User id from which liked comment object will retrieve", required = true)
                                          @Valid @PathVariable(name = "userId") Integer userId,
                                          @ApiParam(value = "Comment id from which liked comment object will retrieve", required = true)
                                          @Valid @PathVariable(name = "commentId") Integer commentId) {
    User userById = userService.getUserById(userId);
    Comment commentById = commentService.getCommentById(commentId);
    return likedCommentService.getLikedCommentByUserAndComment(userById, commentById);
  }

  @ApiOperation(value = "Get a list of all likedComments of a user", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "users/{id}")
  public List<LikedComment> getAllByUser(@ApiParam(value = "User id from which liked comment object will retrieve", required = true)
                                         @Valid @PathVariable(name = "id") Integer userId) {
    User userById = userService.getUserById(userId);
    return likedCommentService.getLikedCommentsByUser(userById);
  }

  @ApiOperation(value = "Get a list of all likedComments of a comment", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "comment/{id}")
  public List<LikedComment> getAllByComment(@ApiParam(value = "Comment id from which liked comment object will retrieve", required = true)
                                            @Valid @PathVariable(name = "id") Integer commentId) {
    Comment commentById = commentService.getCommentById(commentId);
    return likedCommentService.getLikedCommentByComment(commentById);
  }

  @ApiOperation(value = "Get count of liked comments by user", response = Integer.class)
  @GetMapping(path = "users/{id}/count")
  public Integer getCountLikedCommentsByUser(@ApiParam(value = "User id from which count of liked comments objects will retrieve", required = true)
                                             @Valid @PathVariable(name = "id") Integer userId) {
    User userById = userService.getUserById(userId);
    return likedCommentService.getCountLikedCommentByUser(userById);
  }

  @ApiOperation(value = "Get count of liked comment by comment", response = Integer.class)
  @GetMapping(path = "comment/{id}/count")
  public Integer getCountLikedCommentByComment(@ApiParam(value = "Comment id from which count of liked commenrs objects will retrieve", required = true)
                                               @Valid @PathVariable(name = "id") Integer commentId) {
    Comment commentById = commentService.getCommentById(commentId);
    return likedCommentService.getCountLikedCommentByComment(commentById);
  }

  @ApiOperation(value = "Check is like of comment exist", response = boolean.class)
  @GetMapping(path = "{commentId}/exist")
  public boolean isLikeExist(Principal principal,
                             @Valid @PathVariable(name = "commentId") Integer commentId) {
    User userPrincipal = userService.getPrincipal(principal);

    return likedCommentService.isLikeExist(userPrincipal.getId(), commentId);
  }

  @ApiOperation(value = "Create liked comment", response = void.class)
  @PostMapping(path = "/{commentId}")
  public void create(Principal principal,
                     @Valid @PathVariable(name = "commentId") Integer commentId) {
    User userPrincipal = userService.getPrincipal(principal);
    Comment comment = commentService.getCommentById(commentId);

    LikedComment likedComment = new LikedComment();
    likedComment.setUser(userPrincipal);
    likedComment.setComment(comment);
    likedCommentService.createLikedComment(likedComment);
  }

  @ApiOperation(value = "Delete liked comment", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @DeleteMapping(path = "{id}")
  public void delete(@ApiParam(value = "Liked comment Id from which liked comment object will delete from database table", required = true)
                     @Valid @PathVariable(name = "id") Integer likedCommentId) {
    likedCommentService.deleteLikedComment(likedCommentId);
  }
}
