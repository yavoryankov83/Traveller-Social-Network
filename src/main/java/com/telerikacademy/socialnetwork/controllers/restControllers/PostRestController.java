package com.telerikacademy.socialnetwork.controllers.restControllers;

import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.PostDTO;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.PostService;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Api(value = "Social Network Web Application")
@RestController
@RequestMapping(path = "api/v1/posts")
public class PostRestController {

  private PostService postService;
  private UserService userService;

  @Autowired
  public PostRestController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  @ApiOperation(value = "Get a list of all posts", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  public List<Post> getAll() {
    return postService.getAllPosts();
  }

  @ApiOperation(value = "Get a list of all public posts", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "public")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Post> getAllPublic() {
    return postService.getAllPublicPosts();
  }

  @ApiOperation(value = "Get a list of all public posts pageable", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "public/pageable")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Post> getAllPublic(@ApiParam(value = "Current page number to display post objects", required = true)
                                 @Valid @RequestParam(name = "page") Integer page,
                                 @ApiParam(value = "Number of post objects to display on current page", required = true)
                                 @Valid @RequestParam(name = "size") Integer size) {
    return postService.getAllPublicPosts(page, size);
  }

  @ApiOperation(value = "Get a list of all public posts", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "publicDTO")
  @ResponseStatus(code = HttpStatus.OK)
  public List<PostDTO> getAllPublicDTOS() {
    return postService.getAllPublicPostsDTO();
  }

  @ApiOperation(value = "Get post by ID", response = Post.class)
  @GetMapping(path = "{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public Post getById(@ApiParam(value = "Post id from which post object will retrieve", required = true)
                      @Valid @PathVariable(name = "id") Integer postId) {
    return postService.getPostById(postId);
  }

  @ApiOperation(value = "Get all posts by User", response = List.class)
  @GetMapping(path = "users/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Post> getByUser(@ApiParam(value = "User id from which posts objects will retrieve", required = true)
                              @Valid @PathVariable(name = "id") Integer userId) {
    User userById = userService.getUserById(userId);
    return postService.getPostsByUser(userById);
  }

  @ApiOperation(value = "Get all posts by User pageable", response = List.class)
  @GetMapping(path = "users/{id}/pageable")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Post> getByUser(@ApiParam(value = "User id from which posts objects will retrieve", required = true)
                              @Valid @PathVariable(name = "id") Integer userId,
                              @ApiParam(value = "Current page number to display post objects", required = true)
                              @Valid @RequestParam(name = "page") Integer page,
                              @ApiParam(value = "Number of post objects to display on current page", required = true)
                              @Valid @RequestParam(name = "size") Integer size) {
    User userById = userService.getUserById(userId);

    return postService.getPostsByUser(userById, page, size);
  }

  @GetMapping(path = "public/filtered")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Post> getAllByFilter(@RequestParam @Valid String filterParam) {
    return postService.filterPostsByUsernameAndEmailAndFirstNameAndLastName(filterParam);
  }

  @ApiOperation(value = "Get all visible post for user", response = List.class)
  @GetMapping(path = "allFeeds")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Post> getPostsVisibleForUser(@ApiParam(value = "Principal who want to get feeds", required = true)
                                                   Principal principal) {

    return postService.getAllPostsVisibleByPrincipal(principal);
  }

  @ApiOperation(value = "Create post", response = void.class)
  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public void create(@ApiParam(value = "Post object store in database table", required = true)
                     @Valid @RequestBody Post post,
                     Principal principal) {
    User user = userService.getPrincipal(principal);
    post.setUser(user);
    postService.createPost(post);
  }

  @ApiOperation(value = "Update post", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @PutMapping(path = "{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public void update(@ApiParam(value = "Post Id to update post object", required = true)
                     @Valid @PathVariable(name = "id") Integer postId,
                     @ApiParam(value = "Update post object", required = true)
                     @Valid @RequestBody Post post) {
    postService.updatePost(postId, post);
  }

  @ApiOperation(value = "Delete post", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @DeleteMapping(path = "{id}")
  public void delete(@ApiParam(value = "Post Id from which post object will delete from database table", required = true)
                     @Valid @PathVariable(name = "id") Integer postId) {
    postService.deletePost(postId);
  }
}
