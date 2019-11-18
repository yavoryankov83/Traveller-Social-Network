package com.telerikacademy.socialnetwork.controllers.restControllers;

import com.telerikacademy.socialnetwork.models.LikedPost;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.LikedPostService;
import com.telerikacademy.socialnetwork.services.contracts.PostService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Api(value = "Social Network Web Application")
@RestController
@RequestMapping(path = "api/v1/likedPosts")
public class LikedPostRestController {

  private LikedPostService likedPostService;
  private UserService userService;
  private PostService postService;

  @Autowired
  public LikedPostRestController(LikedPostService likedPostService,
                                 UserService userService,
                                 PostService postService) {
    this.likedPostService = likedPostService;
    this.userService = userService;
    this.postService = postService;
  }

  @ApiOperation(value = "Get liked post by ID", response = LikedPost.class)
  @GetMapping(path = "{id}")
  public LikedPost getById(@ApiParam(value = "Liked post id from which liked post object will retrieve", required = true)
                           @Valid @PathVariable(name = "id") Integer likedPostId) {
    return likedPostService.getLikedPostById(likedPostId);
  }

  @ApiOperation(value = "Get a list of all likedPosts of a user", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "users/{id}")
  public List<LikedPost> getAllByUser(@Valid @PathVariable(name = "id") Integer userId) {
    User userById = userService.getUserById(userId);
    return likedPostService.getLikedPostByUser(userById);
  }

  @ApiOperation(value = "Get count of liked post by user", response = Integer.class)
  @GetMapping(path = "users/{id}/count")
  public Integer getCountLikedPostByUser(@ApiParam(value = "User id from which count of liked post objects will retrieve", required = true)
                                         @Valid @PathVariable(name = "id") Integer userId) {
    User userById = userService.getUserById(userId);
    return likedPostService.getCountLikedPostByUser(userById);
  }

  @ApiOperation(value = "Get a list of all likedPosts of a post", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "posts/{id}")
  public List<LikedPost> getAllByPost(@Valid @PathVariable(name = "id") Integer postId) {
    Post postById = postService.getPostById(postId);
    return likedPostService.getLikedPostByPost(postById);
  }

  @ApiOperation(value = "Get count of liked post by post", response = Integer.class)
  @GetMapping(path = "posts/{id}/count")
  public Integer getCountLikedPostByPost(@ApiParam(value = "Post id from which count of liked post objects will retrieve", required = true)
                                         @Valid @PathVariable(name = "id") Integer postId) {
    Post postById = postService.getPostById(postId);
    return likedPostService.getCountLikedPostByPost(postById);
  }

  @ApiOperation(value = "Check is like of post exist", response = boolean.class)
  @GetMapping(path = "{postId}/exist")
  public boolean isLikeExist(Principal principal,
                     @Valid @PathVariable(name = "postId") Integer postId) {
    User userPrincipal = userService.getPrincipal(principal);

    return likedPostService.isLikeExist(userPrincipal.getId(), postId);
  }

  @ApiOperation(value = "Get liked post by user and by post", response = LikedPost.class)
  @GetMapping(path = "users/{userId}/posts/{postId}")
  public LikedPost getByUserAndPost(@ApiParam(value = "User id and post id from which liked post object will retrieve", required = true)
                                    @Valid @PathVariable(name = "userId") Integer userId,
                                    @Valid @PathVariable(name = "postId") Integer postId) {
    User userById = userService.getUserById(userId);
    Post postById = postService.getPostById(postId);
    return likedPostService.getLikedPostByUserAndPost(userById, postById);
  }

  @ApiOperation(value = "Create liked post", response = void.class)
  @PostMapping(path = "posts/{postId}")
  public void create(Principal principal,
                     @Valid @PathVariable(name = "postId") Integer postId) {
    User userPrincipal = userService.getPrincipal(principal);
    Post post = postService.getPostById(postId);
    LikedPost likedPost = new LikedPost();
    likedPost.setUser(userPrincipal);
    likedPost.setPost(post);
    likedPostService.createLikedPost(likedPost);
  }

  @ApiOperation(value = "Delete liked post", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @DeleteMapping(path = "{id}")
  public void delete(@ApiParam(value = "Liked post Id from which liked post object will delete from database table", required = true)
                     @Valid @PathVariable(name = "id") Integer likedPostId) {
    likedPostService.deleteLikedPost(likedPostId);
  }
}
