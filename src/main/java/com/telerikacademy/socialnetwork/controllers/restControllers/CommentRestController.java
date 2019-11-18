package com.telerikacademy.socialnetwork.controllers.restControllers;

import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.CommentService;
import com.telerikacademy.socialnetwork.services.contracts.PostService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Social Network Web Application")
@RestController
@RequestMapping(path = "api/v1/comments")
public class CommentRestController {

  private CommentService commentService;
  private UserService userService;
  private PostService postService;

  @Autowired
  public CommentRestController(CommentService commentService, UserService userService, PostService postService) {
    this.commentService = commentService;
    this.userService = userService;
    this.postService = postService;
  }

  @ApiOperation(value = "Get comment by ID", response = Comment.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "{id}")
  public Comment getById(@ApiParam(value = "Comment id from which comment object will retrieve", required = true)
                         @Valid @PathVariable(name = "id") Integer commentId) {
    return commentService.getCommentById(commentId);
  }

  @ApiOperation(value = "Get a list of all parent comments of a post by post ID", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "parentComment/post/{id}")
  public List<Comment> getAllParentCommentsByPost(@ApiParam(value = "Post id from which parent comment objects will retrieve", required = true)
                                                  @Valid @PathVariable(name = "id") Integer postId) {
    Post postById = postService.getPostById(postId);
    return commentService.getAllParentCommentsByPost(postById);
  }

  @ApiOperation(value = "Get a pageable list of all parent comments of a post by post ID", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "parentComment/post/{id}/pageable")
  public List<Comment> getAllParentCommentsByPost(@ApiParam(value = "Post id from which parent comment objects will retrieve", required = true)
                                                          @Valid @PathVariable(name = "id") Integer postId,
                                                          @ApiParam(value = "Current page number to display parent comment objects", required = true)
                                                          @Valid @RequestParam(name = "page") Integer page,
                                                          @ApiParam(value = "Number of parent comment objects to display on current page", required = true)
                                                          @Valid @RequestParam(name = "size") Integer size
  ) {
    Post postById = postService.getPostById(postId);
    return commentService.getAllParentCommentsByPost(postById, page, size);
  }

  @ApiOperation(value = "Get a list of all child comments of a comment by comment ID", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "childComments/comment/{id}")
  public List<Comment> getAllChildCommentsByParentComment(@Valid @PathVariable(name = "id") Integer parentCommentId) {
    Comment parentComment = commentService.getCommentById(parentCommentId);
    return commentService.getAllChildCommentsByParentComment(parentComment);
  }

  @ApiOperation(value = "Get a list of all comments of a post by post ID", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "post/{id}")
  public List<Comment> getAllByPost(@Valid @PathVariable(name = "id") Integer postId) {
    Post postById = postService.getPostById(postId);
    return commentService.getAllCommentsByPost(postById);
  }

  @ApiOperation(value = "Get a list of all comments by user and by post", response = List.class)
  @GetMapping(path = "users/{userId}/posts/{postId}")
  public List<Comment> getByUserAndPost(@ApiParam(value = "User id and post id from which comments objects will retrieve", required = true)
                                        @Valid @PathVariable(name = "userId") Integer userId,
                                        @Valid @PathVariable(name = "postId") Integer postId) {
    User userById = userService.getUserById(userId);
    Post postById = postService.getPostById(postId);
    return commentService.getAllCommentsByUserAndPost(userById, postById);
  }

  @ApiOperation(value = "Create comment", response = void.class)
  @PostMapping
  public void create(@ApiParam(value = "Comment object store in database table", required = true)
                     @Valid @RequestBody Comment comment) {
    commentService.createComment(comment);
  }

  @ApiOperation(value = "Update comment", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @PutMapping(path = "{id}")
  public void update(@ApiParam(value = "Comment Id to update comment object", required = true)
                     @Valid @PathVariable(name = "id") Integer commentId,
                     @ApiParam(value = "Update comment object", required = true)
                     @Valid @RequestBody Comment comment) {
    commentService.updateComment(commentId, comment);
  }

  @ApiOperation(value = "Delete comment", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @DeleteMapping(path = "{id}")
  public void delete(@ApiParam(value = "Comment Id from which comment object will delete from database table", required = true)
                     @Valid @PathVariable(name = "id") Integer commentId) {
    commentService.deleteComment(commentId);
  }
}
