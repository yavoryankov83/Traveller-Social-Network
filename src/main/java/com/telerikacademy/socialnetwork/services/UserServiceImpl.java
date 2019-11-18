package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.ConflictException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.helper.Constants;
import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements com.telerikacademy.socialnetwork.services.contracts.UserService {

  private UserRepository userRepository;
  private PostRepository postRepository;
  private CommentRepository commentRepository;
  private LikedPostRepository likedPostRepository;
  private LikedCommentRepository likedCommentRepository;
  private FriendShipRepository friendShipRepository;
  private UserDetailsManager userDetailsManager;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository,
                         PostRepository postRepository,
                         CommentRepository commentRepository,
                         LikedPostRepository likedPostRepository,
                         LikedCommentRepository likedCommentRepository,
                         FriendShipRepository friendShipRepository,
                         UserDetailsManager userDetailsManager,
                         PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.likedPostRepository = likedPostRepository;
    this.likedCommentRepository = likedCommentRepository;
    this.friendShipRepository = friendShipRepository;
    this.userDetailsManager = userDetailsManager;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAllByEnabledTrueOrderByUpdateDateDesc();
  }

  @Override
  public List<User> getAllUsersPageable(Integer page, Integer size) {
    return userRepository.findAllByEnabledTrueOrderByUpdateDateDesc(createPageRequest(page, size));
  }

  private Pageable createPageRequest(Integer page, Integer size) {
    return PageRequest.of(page, size);
  }

  @Override
  public List<User> filterByUsernameAndEmailAndFirstNameAndLastName(String filterParam) {
    return userRepository.findAllByEnabledTrueAndUsernameStartingWithOrEmailStartingWithOrFirstNameStartingWithOrLastNameStartingWithOrderByUpdateDateDesc
            (filterParam.toLowerCase(), filterParam.toLowerCase(), filterParam.toLowerCase(), filterParam.toLowerCase());
  }

  @Override
  public User getUserById(Integer userId) {
    return userRepository.findByIdAndEnabledTrue(userId)
            .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository.findByUsernameAndEnabledTrue(username)
            .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
  }

  @Override
  public User getPrincipal(Principal principal) {
    String principalName = principal.getName();

    Optional<User> currentPrincipal = userRepository.findByUsernameAndEnabledTrue(principalName);
    if (!currentPrincipal.isPresent()) {
      throw new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE);
    }
    return currentPrincipal.get();
  }

  @Override
  public Integer getUserRolesCount(String username){
    return userRepository.getUserRolesCount(username);
  }

  @Override
  public void createUser(User user) {
    checkUsernameValidity(user);
    checkPasswordValidity(user);
    checkPasswordConfirmationValidity(user);
    checkEmailValidity(user);

    checkUsernameExists(user);
    checkPasswordMatch(user);
    checkEmailExists(user);

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

    org.springframework.security.core.userdetails.User newUser =
            new org.springframework.security.core.userdetails.User(
                    user.getUsername(), passwordEncoder.encode(user.getPassword()), authorities);
    userDetailsManager.createUser(newUser);

    User userToCreate = getUserByUsername(user.getUsername());

    userToCreate.setEmail(user.getEmail());
    checkUserPhotoValidity(user, userToCreate);
    checkCoverPhotoValidity(user, userToCreate);
    checkUserFirstNameValidity(user, userToCreate);
    checkUserLastNameValidity(user, userToCreate);
    userToCreate.setEnabled(user.getEnabled());
    userToCreate.setUserPhotoVisibility(user.getUserPhotoVisibility());
    userToCreate.setNameVisibility(user.getNameVisibility());

    userRepository.save(userToCreate);
  }

  @Override
  @Transactional
  public void updateUser(Integer userId, User user) {
    checkUsernameValidity(user);
    checkPasswordValidity(user);
    checkPasswordConfirmationValidity(user);
    checkEmailValidity(user);

    checkUsernameExists(userId, user);
    checkPasswordMatch(user);
    checkEmailExists(userId, user);

    User userToUpdate = getUserById(userId);

    Collection<? extends GrantedAuthority> authorities =
            userDetailsManager.loadUserByUsername(userToUpdate.getUsername()).getAuthorities();

    org.springframework.security.core.userdetails.User newUser =
            new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    passwordEncoder.encode(user.getPassword()),
                    authorities);

    userDetailsManager.updateUser(newUser);

    userToUpdate.setEmail(user.getEmail());
    checkUserPhotoIsSet(user, userToUpdate);
    checkCoverPhotoIsSet(user, userToUpdate);
    checkUserFirstNameValidity(user, userToUpdate);
    checkUserLastNameValidity(user, userToUpdate);
    userToUpdate.setNameVisibility(user.getNameVisibility());
    userToUpdate.setUserPhotoVisibility(user.getUserPhotoVisibility());

    userRepository.save(userToUpdate);
  }

  @Override
  @Transactional
  public void deleteUser(Integer userId) {
    User userToDelete = getUserById(userId);

    deleteUserPosts(userToDelete);

    deleteUserComments(userToDelete);

    deleteUserLikedPosts(userToDelete);

    deleteUserLikedComments(userToDelete);

    deleteUserFriendShips(userToDelete);

    userToDelete.setEnabled(false);

    userRepository.save(userToDelete);
  }

  private void deleteUserFriendShips(User userToDelete) {
    friendShipRepository.findAllUserFriendShips(userToDelete.getId())
            .forEach(friendship -> {
              friendship.setEnabled(false);
              friendShipRepository.save(friendship);
            });
  }

  private void deleteUserLikedComments(User userToDelete) {
    likedCommentRepository.findAllByUserAndEnabledTrue(userToDelete)
            .forEach(likedComment -> {
              likedComment.setEnabled(false);
              likedCommentRepository.save(likedComment);
            });
  }

  private void deleteUserLikedPosts(User userToDelete) {
    likedPostRepository.findAllByUserAndEnabledTrue(userToDelete)
            .forEach(likedPost -> {
              likedPost.setEnabled(false);
              likedPostRepository.save(likedPost);
            });
  }

  private void deleteUserComments(User userToDelete) {
    commentRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(userToDelete)
            .forEach(comment -> {
              comment.setEnabled(false);
              commentRepository.save(comment);
            });
  }

  private void deleteUserPosts(User userToDelete) {
    postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(userToDelete)
            .forEach(post -> {
              post.setEnabled(false);
              postRepository.save(post);
            });
  }

  private void checkEmailValidity(User user) {
    if (Constants.EMPTY_STRING.equals(user.getEmail())) {
      throw new BadRequestException(Constants.USER_EMAIL_EMPTY_MESSAGE);
    }
  }

  private void checkPasswordConfirmationValidity(User user) {
    if (Constants.EMPTY_STRING.equals(user.getPasswordConfirmation())) {
      throw new BadRequestException(Constants.USER_PASSWORD_CONFIRMATION_EMPTY_MESSAGE);
    }
  }

  private void checkPasswordValidity(User user) {
    if (Constants.EMPTY_STRING.equals(user.getPassword())) {
      throw new BadRequestException(Constants.USER_PASSWORD_EMPTY_MESSAGE);
    }
  }

  private void checkUsernameValidity(User user) {
    if (Constants.EMPTY_STRING.equals(user.getUsername())) {
      throw new BadRequestException(Constants.USER_USERNAME_EMPTY_MESSAGE);
    }
  }

  //using to check create
  private void checkUsernameExists(User user) {
    if (userRepository.existsUserByUsernameAndEnabledTrue(user.getUsername())) {
      throw new ConflictException(Constants.USER_ALREADY_EXISTS_MESSAGE);
    }
  }

  //using to check update
  private void checkUsernameExists(Integer userId, User user) {
    if (userRepository.existsUserByUsernameAndEnabledTrueAndIdIsNotLike(user.getUsername(), userId)) {
      throw new ConflictException(Constants.USER_ALREADY_EXISTS_MESSAGE);
    }
  }

  private void checkPasswordMatch(User user) {
    if (!user.getPassword().equals(user.getPasswordConfirmation())) {
      throw new BadRequestException(Constants.USER_PASSWORD_NOT_MATCH_CONFIRMATION_PASSWORD_MESSAGE);
    }
  }

  //using to check create
  private void checkEmailExists(User user) {
    if (userRepository.existsUserByEmailAndEnabledTrue(user.getEmail())) {
      throw new ConflictException(Constants.USER_EMAIL_ALREADY_EXISTS_MESSAGE);
    }
  }

  //using to check update
  private void checkEmailExists(Integer userId, User user) {
    if (userRepository.existsUserByEmailAndEnabledTrueAndIdIsNotLike(user.getEmail(), userId)) {
      throw new ConflictException(Constants.USER_EMAIL_ALREADY_EXISTS_MESSAGE);
    }
  }

  private void checkUserLastNameValidity(User user, User userToCreate) {
    if (Constants.EMPTY_STRING.equals(user.getLastName())) {
      userToCreate.setLastName(Constants.USER_DEFAULT_NAME);
    } else {
      userToCreate.setLastName(user.getLastName());
    }
  }

  private void checkUserFirstNameValidity(User user, User userToCreate) {
    if (Constants.EMPTY_STRING.equals(user.getFirstName())) {
      userToCreate.setFirstName(Constants.USER_DEFAULT_NAME);
    } else {
      userToCreate.setFirstName(user.getFirstName());
    }
  }

  private void checkCoverPhotoValidity(User user, User userToCreate) {
    if (user.getCoverPhoto() != null) {
      userToCreate.setCoverPhoto(user.getCoverPhoto());
    } else {
      setDefaultCoverPhoto(userToCreate);
    }
  }

  private void checkUserPhotoValidity(User user, User userToCreate) {
    if (user.getUserPhoto() != null) {
      userToCreate.setUserPhoto(user.getUserPhoto());
    } else {
      setDefaultUserPhoto(userToCreate);
    }
  }

  private void setDefaultUserPhoto(User userToCreate) {
    File defaultUserPhoto = new File(Constants.USER_DEFAULT_PHOTO);
    String filePath = defaultUserPhoto.getAbsolutePath();

    File file = new File(filePath);
    byte[] userDefaultPhoto = new byte[(int) file.length()];

    try {
      userDefaultPhoto = Helper.convertFileContentToBlob(filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    userToCreate.setUserPhoto(userDefaultPhoto);
  }

  private void setDefaultCoverPhoto(User userToCreate) {
    File defaultCoverPhoto = new File(Constants.COVER_DEFAULT_PHOTO);
    String filePath = defaultCoverPhoto.getAbsolutePath();

    File file = new File(filePath);
    byte[] coverDefaultPhoto = new byte[(int) file.length()];

    try {
      coverDefaultPhoto = Helper.convertFileContentToBlob(filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    userToCreate.setCoverPhoto(coverDefaultPhoto);
  }

  private void checkCoverPhotoIsSet(User user, User userToUpdate) {
    if (user.getCoverPhoto() != null) {
      userToUpdate.setCoverPhoto(user.getCoverPhoto());
    }
  }

  private void checkUserPhotoIsSet(User user, User userToUpdate) {
    if (user.getUserPhoto() != null) {
      userToUpdate.setUserPhoto(user.getUserPhoto());
    }
  }
}
