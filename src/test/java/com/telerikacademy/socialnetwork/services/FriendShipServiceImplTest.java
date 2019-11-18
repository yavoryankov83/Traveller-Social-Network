package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.models.Friendship;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.FriendShipRepository;
import com.telerikacademy.socialnetwork.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.*;

import static org.mockito.Mockito.*;

public class FriendShipServiceImplTest {
    @Mock
    FriendShipRepository friendShipRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    FriendShipServiceImpl friendShipServiceImpl;

    private Friendship friendship;
    private Friendship friendship2;
    private User user;
    private User user2;
    private Principal principal;
    private List<Friendship>friendshipList;
    private List<Integer> friendsIdList;
    private List<User> friendsList;
    private Set<User>nonFriend;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        friendship = Mockito.mock(Friendship.class);
        friendship2 = Mockito.mock(Friendship.class);
        user = Mockito.mock(User.class);
        user2 = Mockito.mock(User.class);
        principal = Mockito.mock(Principal.class);
        friendshipList = new ArrayList<>();
        friendshipList.add(friendship);
        friendsIdList = new ArrayList<>();
        friendsIdList.add(1);
        friendsList = new ArrayList<>();
        friendsList.add(user);
        nonFriend = new HashSet<>();

    }

    @Test
    public void get_friendship_should_return_friendfhip(){
        //Act
        when(friendShipRepository.findIfUsersAreFriends(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        //Arrange
        Friendship result = friendShipServiceImpl.getFriendship(1, 2);
        //Assert
        Assert.assertEquals(friendship, result);
    }

    @Test
    public void get_if_relation_exists_should_return_friendfhip(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        //Arrange
        Friendship result = friendShipServiceImpl.getIfRelationExists(1, 2);
        //Assert
        Assert.assertEquals(friendship, result);
    }

    @Test
    public void have_users_friendfhip_with_status_friends_should_return_boolean(){
        //Act
        when(friendShipRepository.findIfUsersAreFriends(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        boolean result = friendShipServiceImpl.isUsersAreFriends(principal, 1);
        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void have_users_friendfhip_with_status_friends_should_return_false(){
        //Act
        when(friendShipRepository.findIfUsersAreFriends(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(1);
        when(principal.getName()).thenReturn("principal");
        //Arrange
        boolean result = friendShipServiceImpl.isUsersAreFriends(principal, 1);
        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void have_users_friendfhip_with_any_status_should_return_boolean(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        boolean result = friendShipServiceImpl.hasUsersRelation(principal, 1);
        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void have_users_friendfhip_with_any_status_should_return_false(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        when(user.getId()).thenReturn(1);
        //Arrange
        boolean result = friendShipServiceImpl.hasUsersRelation(principal, 1);
        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void did_principal_block_user_should_return_boolean(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        boolean result = friendShipServiceImpl.isBlockedUserByPrincipal(principal, 1);
        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void did_principal_block_user_should_return_false(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        when(user.getId()).thenReturn(1);
        //Arrange
        boolean result = friendShipServiceImpl.isBlockedUserByPrincipal(principal, 1);
        //Assert
        Assert.assertFalse(result);
    }
    @Test
    public void did_principal_block_user_should_return_true(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        when(friendship.getStatusId()).thenReturn(4);
        when(friendship.getUser()).thenReturn(user);
        //Arrange
        boolean result = friendShipServiceImpl.isBlockedUserByPrincipal(principal, 1);
        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void has_principal_requests_should_return_boolean(){
        //Act
        when(friendShipRepository.findRequestSendByUser(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        boolean result = friendShipServiceImpl.hasPrincipalRequest(1, principal);
        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void has_principal_requests_should_return_false(){
        //Act
        when(friendShipRepository.findRequestSendByUser(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        when(user.getId()).thenReturn(1);
        //Arrange
        boolean result = friendShipServiceImpl.hasPrincipalRequest(1, principal);
        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void get_all_user_friendShips_should_return_list_of_friendships(){
        //Act
        when(friendShipRepository.findAllUserFriendShips(anyInt())).thenReturn(friendshipList);
        //Arrange
        List<Friendship> result = friendShipServiceImpl.getAllUserFriendShips(1);
        //Assert
        Assert.assertEquals(friendshipList.size(), result.size());
    }

    @Test
    public void get_all_friends_of_principal_should_return_list_of_friendships_with_status_accepted_friend(){
        //Act
        when(friendShipRepository.findAllFriendsId(anyInt())).thenReturn(friendsIdList);
        when(friendShipRepository.findAllUsersId(anyInt())).thenReturn(friendsIdList);
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        List<User> result = friendShipServiceImpl.getAllUserFriends(principal);
        //Assert
        Assert.assertEquals(friendsList.get(0), result.get(0));
    }

    @Test
    public void get_all_requests_of_principal_should_return_list_of_friendships_with_status_pending(){
        //Act
        when(friendShipRepository.findRequestToPrincipal(anyInt())).thenReturn(friendsIdList);
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        List<User> result = friendShipServiceImpl.getAllUserSentMeRequest(principal);
        //Assert
        Assert.assertEquals(friendsList.size(), result.size());
    }

    @Test(expected = NotFoundException.class)
    public void get_all_friends_of_invalid_user_should_throw_exception(){
        //Act
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(friendShipRepository.findAllFriendsId(anyInt())).thenReturn(friendsIdList);
        when(friendShipRepository.findAllUsersId(anyInt())).thenReturn(friendsIdList);
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
        //Arrange
        List<User> result = friendShipServiceImpl.getUserFriends(1);
    }

    @Test
    public void get_all_friends_of_user_should_return_list_of_friendships_with_status_accepted_friend(){
        //Act
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(friendShipRepository.findAllFriendsId(anyInt())).thenReturn(friendsIdList);
        when(friendShipRepository.findAllUsersId(anyInt())).thenReturn(friendsIdList);
        when(userRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(user));
        //Arrange
        List<User> result = friendShipServiceImpl.getUserFriends(1);
        //Assert
        Assert.assertEquals(friendsList.get(0), result.get(0));
    }

    @Test
    public void get_all_nonFriends_should_return_list_of_userd_without_relation(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findAllByEnabledTrueOrderByUpdateDateDesc()).thenReturn(friendsList);
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        Set<User> result = friendShipServiceImpl.getAllNonRelation(principal);
        //Assert
        Assert.assertEquals(nonFriend.size(), result.size());
    }

    @Test
    public void get_all_nonFriends_should_return_list_of_userd_without_relation1(){
        //Act
        List<User>friendsList = new ArrayList<>();
        User user = new User();
        user.setId(3);
        friendsList.add(user);
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(userRepository.findAllByEnabledTrueOrderByUpdateDateDesc()).thenReturn(friendsList);
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user2));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        Set<User> result = friendShipServiceImpl.getAllNonRelation(principal);
        //Assert
        Assert.assertEquals(friendsList.size(), result.size());
    }

    @Test(expected = NotFoundException.class)
    public void send_friendShip_request_to_existing_friendShip_should_throw_exception(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        //Arrange
        friendShipServiceImpl.sendFriendShipRequest(principal, 2);
    }

    @Test
    public void send_FriendShip_request_when_data_valid(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        //Arrange
        friendShipServiceImpl.sendFriendShipRequest(principal, 2);
        //Assert
        Assert.assertEquals(1, friendShipRepository.findAll().size());
    }

    @Test(expected = NotFoundException.class)
    public void update_invalid_friendShip_request_should_throw_exception(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(friendShipRepository.getFriendShipWherePrincipalIsFriend(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(principal.getName()).thenReturn("principal");
        //Arrange
        friendShipServiceImpl.updateFriendShipRequest(1, principal, 3);
    }

    @Test
    public void update_friendShip_request_when_data_valid(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(friendShipRepository.getFriendShipWherePrincipalIsFriend(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(principal.getName()).thenReturn("principal");
        //Arrange
        friendShipServiceImpl.updateFriendShipRequest(1, principal, 3);
        //Assert
        Assert.assertEquals(1, friendShipRepository.findAll().size());
    }

    @Test
    public void accept_friendship_request_when_data_valid(){
        //Act
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        //Arrange
        friendShipServiceImpl.acceptFriendshipRequest(1, 2);
        //Assert
        Assert.assertEquals(1, friendShipRepository.findAll().size());
    }

    @Test
    public void reject_friendship_request_when_data_valid(){
        //Act
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        //Arrange
        friendShipServiceImpl.rejectFriendshipRequest(1, 2);
        //Assert
        Assert.assertEquals(1, friendShipRepository.findAll().size());
    }

    @Test(expected = NotFoundException.class)
    public void block_friendship_should_throw_exception(){
        //Act
        when(friendShipRepository.findIfUsersAreFriends(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(principal.getName()).thenReturn("principal");
        when(friendship.getUser()).thenReturn(user);
        //Arrange
        friendShipServiceImpl.blockFriendshipRequest(1, principal);
    }

    @Test
    public void block_friendship_when_data_valid(){
        //Act
        Friendship friendship = new Friendship();
        friendship.setStatusId(2);
        User user2 = new User();
        friendship.setUser(user2);
        user2.setId(1);
        when(friendShipRepository.findIfUsersAreFriends(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(principal.getName()).thenReturn("principal");
        //Arrange
        friendShipServiceImpl.blockFriendshipRequest(1, principal);
        //Assert
        Assert.assertEquals(1, friendShipRepository.findAll().size());
    }

    @Test(expected = NotFoundException.class)
    public void unblock_friendship_with_invalid_principal_should_throw_exception(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.empty());
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(friendship.getStatusId()).thenReturn(4);
        when(principal.getName()).thenReturn("principal");
        //Arrange
        friendShipServiceImpl.unblockFriendshipRequest(principal, 1);
    }

    @Test
    public void unblock_friendship_when_data_valid(){
        //Act
        when(friendShipRepository.checkIfRelationExistsBetweenUsers(anyInt(), anyInt())).thenReturn(Optional.of(friendship));
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(friendShipRepository.findAll()).thenReturn(friendshipList);
        when(friendship.getStatusId()).thenReturn(4);
        when(principal.getName()).thenReturn("principal");
        //Arrange
        friendShipServiceImpl.unblockFriendshipRequest(principal, 1);
        //Assert
        Assert.assertEquals(1, friendShipRepository.findAll().size());
    }
}