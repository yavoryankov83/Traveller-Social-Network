package com.telerikacademy.socialnetwork.controllers.restControllers;

import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.services.contracts.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Social Network Web Application")
@RestController
@RequestMapping(path = "api/v1/users")
public class UserRestController {

  private UserService userService;

  @Autowired
  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @ApiOperation(value = "Get a list of all users", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping
  public List<User> getAll() {
    return userService.getAllUsers();
  }

  @ApiOperation(value = "Get a list of all users pageable", response = List.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully retrieved list"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "pageable")
  public List<User> getAllPageable(@Valid @RequestParam(name = "page") Integer page,
                                   @Valid @RequestParam(name = "size") Integer size) {
    return userService.getAllUsersPageable(page, size);
  }

  @GetMapping(path = "filtered")
  public List<User> getAllByFilter(@RequestParam @Valid String filterParam) {
    return userService.filterByUsernameAndEmailAndFirstNameAndLastName(filterParam);
  }

  @ApiOperation(value = "Get an user by ID", response = User.class)
  @ApiResponses(value = {
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "{id}")
  public User getById(@ApiParam(value = "User id from which user object will retrieve", required = true)
                      @Valid @PathVariable(name = "id") Integer userId) {
    return userService.getUserById(userId);
  }

  @ApiOperation(value = "Get an user by username", response = User.class)
  @ApiResponses(value = {
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "username/{username}")
  public User getByUsername(@ApiParam(value = "User username from which user object will retrieve", required = true)
                            @Valid @PathVariable(name = "username") String username) {
    return userService.getUserByUsername(username);
  }

  @ApiOperation(value = "Create an user", response = void.class)
  @PostMapping
  public void create(@ApiParam(value = "Create user object", required = true)
                     @Valid @RequestBody User user) {
    userService.createUser(user);
  }

  @GetMapping(path = "{username}/rolesCount")
  public Integer getUserRolesCount(@Valid @PathVariable(name = "username") String username){
    return userService.getUserRolesCount(username);
  }

  @ApiOperation(value = "Update an user", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @PutMapping(path = "{id}")
  public void update(@ApiParam(value = "User Id to update user object", required = true)
                     @Valid @PathVariable(name = "id") Integer userId,
                     @ApiParam(value = "Update user object", required = true)
                     @Valid @RequestBody User user) {
    userService.updateUser(userId, user);
  }

  @ApiOperation(value = "Delete an user", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @DeleteMapping(path = "{id}")
  public void delete(@ApiParam(value = "User Id from which user object will delete from database table", required = true)
                     @Valid @PathVariable(name = "id") Integer userId) {
    userService.deleteUser(userId);
  }
}
