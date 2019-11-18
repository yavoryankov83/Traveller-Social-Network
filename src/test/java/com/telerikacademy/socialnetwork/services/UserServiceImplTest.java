package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.ConflictException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.models.*;
import com.telerikacademy.socialnetwork.repositories.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    LikedPostRepository likedPostRepository;
    @Mock
    LikedCommentRepository likedCommentRepository;
    @Mock
    FriendShipRepository friendShipRepository;
    @Mock
    UserDetailsManager userDetailsManager;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    private User user;
    private Principal principal;
    private List<User> userList;
    private List<Post> postList;
    private Comment comment;
    private LikedComment likedComment;
    private LikedPost likedPost;
    private Post post;
    private Friendship friendship;
    private List<Comment> commentList;
    private List<LikedComment> likedComments;
    private List<LikedPost> likedPosts;
    private List<Friendship>friendshipList;
    private UserDetails userDetails;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = Mockito.mock(User.class);
        principal = Mockito.mock(Principal.class);
        post = Mockito.mock(Post.class);
        comment = Mockito.mock(Comment.class);
        likedPost = Mockito.mock(LikedPost.class);
        likedComment = Mockito.mock(LikedComment.class);
        friendship = Mockito.mock(Friendship.class);
        userDetails = Mockito.mock(UserDetails.class);
        userList = new ArrayList<>();
        userList.add(user);
        postList = new ArrayList<>();
        commentList = new ArrayList<>();
        likedComments = new ArrayList<>();
        likedPosts = new ArrayList<>();
        friendshipList = new ArrayList<>();
    }

    @Test
    public void get_all_users_should_return_list_of_users(){
        //Act
        when(userRepository.findAllByEnabledTrueOrderByUpdateDateDesc()).thenReturn(userList);
        //Arrange
        List<User> result = userServiceImpl.getAllUsers();
        //Assert
        Assert.assertEquals(userList.size(), result.size());
    }

    @Test
    public void get_all_users_pageable_should_return_list_of_users(){
        //Act
        when(userRepository.findAllByEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(userList);
        //Arrange
        List<User> result = userServiceImpl.getAllUsersPageable(1, 1);
        //Assert
        Assert.assertEquals(userList.size(), result.size());
    }

    @Test
    public void filter_by_username_email_firstName_lastName(){
        //Act
        when(userRepository.findAllByEnabledTrueAndUsernameStartingWithOrEmailStartingWithOrFirstNameStartingWithOrLastNameStartingWithOrderByUpdateDateDesc(anyString(), anyString(), anyString(), anyString())).thenReturn(userList);
        //Arrange
        List<User> result = userServiceImpl.filterByUsernameAndEmailAndFirstNameAndLastName("filterParam");
        //Assert
        Assert.assertEquals(userList.size(), result.size());
    }

    @Test
    public void get_user_by_id_should_return_user(){
        //Act
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.ofNullable(user));
        //Arrange
        User result = userServiceImpl.getUserById(1);
        //Assert
        Assert.assertEquals(user, result);
    }

    @Test
    public void get_user_by_username_should_return_user(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        //Arrange
        User result = userServiceImpl.getUserByUsername("username");
        //Assert
        Assert.assertEquals(user, result);
    }

    @Test
    public void get_principal_should_return_user(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        User result = userServiceImpl.getPrincipal(principal);
        //Assert
        Assert.assertEquals(user, result);
    }

    @Test(expected = NotFoundException.class)
    public void throw_exception_when_get_invalid_principal(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.empty());
        when(principal.getName()).thenReturn("principal");
        //Arrange
        User result = userServiceImpl.getPrincipal(principal);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_create_user_when_password_invalid(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("");
        when(user.getPasswordConfirmation()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.createUser(user);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_create_user_when_username_invalid(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.createUser(user);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_create_user_when_passwordConfirmation_invalid(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.createUser(user);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_create_user_when_email_invalid(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("password");
        when(user.getEmail()).thenReturn("");
        //Arrange
        userServiceImpl.createUser(user);
    }

    @Test(expected = ConflictException.class)
    public void throw_exception_create_user_when_user_exists(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(true);
        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.createUser(user);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_create_user_when_password_notMatch(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("password2");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.createUser(user);
    }

    @Test(expected = ConflictException.class)
    public void throw_exception_create_user_when_email_exists(){
        //Act
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(true);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.createUser(user);
    }

//    @Test
//    public void create_user_when_data_valid(){
//        //Act
//        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
//        when(userRepository.existsUserByUsernameAndEnabledTrue(anyString())).thenReturn(false);
//        when(userRepository.existsUserByEmailAndEnabledTrue(anyString())).thenReturn(false);
//        when(userRepository.findAll()).thenReturn(userList);
//        when(passwordEncoder.encode(user.getPassword())).thenReturn("password");
//        when(user.getUsername()).thenReturn("username");
//        when(user.getPassword()).thenReturn("password");
//        when(user.getPasswordConfirmation()).thenReturn("password");
//        when(user.getEmail()).thenReturn("email");
//        //Arrange
//        userServiceImpl.createUser(user);
//        //Assert
//        Assert.assertEquals(1, postRepository.findAll().size());
//    }

    @Test(expected = ConflictException.class)
    public void throw_exception_update_user_when_email_exists(){
        //Act
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrueAndIdIsNotLike(anyString(), anyInt())).thenReturn(true);
        when(userRepository.existsUserByEmailAndEnabledTrueAndIdIsNotLike(anyString(), anyInt())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.updateUser(1, user);
    }

    @Test(expected = ConflictException.class)
    public void throw_exception_update_user_when_email_invalid(){
        //Act
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByUsernameAndEnabledTrueAndIdIsNotLike(anyString(), anyInt())).thenReturn(false);
        when(userRepository.existsUserByEmailAndEnabledTrueAndIdIsNotLike(anyString(), anyInt())).thenReturn(true);
        when(userRepository.findAll()).thenReturn(userList);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getPasswordConfirmation()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        //Arrange
        userServiceImpl.updateUser(1, user);
    }

//    @Test
////    @WithUserDetails(value = "test@test.de", userDetailsServiceBeanName = "loadUserByUsername")
//    public void update_user_when_data_valid(){
//        //Act
//        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
//        when(userRepository.existsUserByUsernameAndEnabledTrueAndIdIsNotLike(anyString(), anyInt())).thenReturn(false);
//        when(userRepository.existsUserByEmailAndEnabledTrueAndIdIsNotLike(anyString(), anyInt())).thenReturn(false);
//        when(userRepository.findAll()).thenReturn(userList);
//        Authentication authentication = Mockito.mock(Authentication.class);
//
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        when(user.getUsername()).thenReturn("username");
//        when(user.getPassword()).thenReturn("password");
//        when(user.getPasswordConfirmation()).thenReturn("password");
//        when(user.getEmail()).thenReturn("email");
//        //Arrange
//        userServiceImpl.updateUser(1, user);
//        //Assert
//        Assert.assertEquals(1, postRepository.findAll().size());
//    }

    @Test
    public void delete_user_when_data_valid(){
        //Act
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(postList);
        when(commentRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(commentList);
        when(likedPostRepository.findAllByUserAndEnabledTrue(any())).thenReturn(likedPosts);
        when(likedCommentRepository.findAllByUserAndEnabledTrue(any())).thenReturn(likedComments);
        when(friendShipRepository.findAllUserFriendShips(anyInt())).thenReturn(friendshipList);
        friendshipList.add(friendship);
        likedComments.add(likedComment);
        likedPosts.add(likedPost);
        commentList.add(comment);
        postList.add(post);
        //Arrange
        userServiceImpl.deleteUser(1);
        //Assert
        Assert.assertEquals(0, postRepository.findAll().size());
    }
}