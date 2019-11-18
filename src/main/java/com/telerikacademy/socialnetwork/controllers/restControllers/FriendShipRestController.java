package com.telerikacademy.socialnetwork.controllers.restControllers;

import com.telerikacademy.socialnetwork.models.Friendship;
import com.telerikacademy.socialnetwork.services.FriendShipServiceImpl;
import com.telerikacademy.socialnetwork.services.contracts.FriendShipService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Api(value = "Social Network Web Application")
@RestController
@RequestMapping(path = "api/v1/friendShip")
public class FriendShipRestController {

  private FriendShipService friendShipService;

  @Autowired
  public FriendShipRestController(FriendShipServiceImpl friendShipService) {
    this.friendShipService = friendShipService;
  }

  @ApiOperation(value = "Get friendship request between two users", response = Friendship.class)
  @ApiResponses(value = {
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "users/{userId}/friends/{friendId}")
  public Friendship getFriendship(@ApiParam(value = "User Id to retrieve user object who send a request", required = true)
                                  @Valid @PathVariable(name = "userId") Integer userId,
                                  @ApiParam(value = "User Id to retrieve user object to whom was send a request", required = true)
                                  @Valid @PathVariable(name = "friendId") Integer friendId) {
    return friendShipService.getIfRelationExists(userId, friendId);
  }

  @ApiOperation(value = "Create a friendship request", response = void.class)
  @PostMapping(path = "{userId}")
  public void createFriendShip(@ApiParam(value = "User object who send a request", required = true)
                                       Principal principal,
                               @ApiParam(value = "User Id to retrieve user object to whom was send a request", required = true)
                               @Valid @PathVariable(name = "userId") Integer userId) {
    friendShipService.sendFriendShipRequest(principal, userId);
  }

  @ApiOperation(value = "Update a friendship request", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @PutMapping(path = "{userId}/{status}")
  public void updateFriendShipRequest(@ApiParam(value = "User Id to retrieve user object who send a request", required = true)
                               @Valid @PathVariable(name = "userId") Integer userId,
                                      @ApiParam(value = "User object whom can set status to request", required = true)
                                       Principal principal,
                                      @ApiParam(value = "Value to set to friendship request", required = true)
                               @Valid @PathVariable(name = "status") Integer status) {
    friendShipService.updateFriendShipRequest(userId, principal, status);
  }

  @ApiOperation(value = "Unblock a blocked friendship", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @PutMapping(path = "{userId}/unblock")
  public void unblockFriendShip(@ApiParam(value = "User Id to retrieve user object whom is unblocked by a friend who already blocked him before", required = true)
                              @Valid @PathVariable(name = "userId") Integer userId,
                              @ApiParam(value = "User object who unblocks", required = true)
                                      Principal principal) {
    friendShipService.unblockFriendshipRequest(principal, userId);
  }

  @ApiOperation(value = "Block a friendship", response = void.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @PutMapping(path = "{userId}/block")
  public void blockFriendShip(@ApiParam(value = "User Id to retrieve user object whom is blocked by a friend", required = true)
                              @Valid @PathVariable(name = "userId") Integer userId,
                              @ApiParam(value = "User object who blocks", required = true)
                                      Principal principal) {
    friendShipService.blockFriendshipRequest(userId,principal);
  }

  @ApiOperation(value = "Check if users are friends", response = boolean.class)
  @ApiResponses(value = {
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  @GetMapping(path = "friends/user/{userId}")
  public boolean isUsersFriends(@ApiParam(value = "User Id to retrieve user object to check if is a friend", required = true)
                                @Valid @PathVariable(name = "userId") Integer userId,
                                @ApiParam(value = "User object to check if is a friend", required = true)
                                        Principal principal){
    return friendShipService.isUsersAreFriends(principal, userId);
  }
}
