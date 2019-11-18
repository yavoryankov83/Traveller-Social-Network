package com.telerikacademy.socialnetwork.services.contracts;

import com.telerikacademy.socialnetwork.models.User;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

public interface UserService {

  List<User> getAllUsers();

  List<User> getAllUsersPageable(Integer page, Integer size);

  List<User> filterByUsernameAndEmailAndFirstNameAndLastName(String filterParam);

  User getUserById(Integer userId);

  User getUserByUsername(String username);

  User getPrincipal(Principal principal);

  Integer getUserRolesCount(String username);

  void createUser(User user);

  @Transactional
  void updateUser(Integer userId, User user);

  @Transactional
  void deleteUser(Integer userId);
}
