package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.models.LikedPost;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.LikedPostRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class LikedPostServiceImplTest {
    @Mock
    LikedPostRepository likedPostRepository;
    @InjectMocks
    LikedPostServiceImpl likedPostServiceImpl;

    private LikedPost likedPost;
    private User user;
    private Post post;
    private List<LikedPost>likedPosts;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        likedPost = Mockito.mock(LikedPost.class);
        user = Mockito.mock(User.class);
        post = Mockito.mock(Post.class);
        likedPosts = new ArrayList<>();
        likedPosts.add(likedPost);
    }

    @Test
    public void get_likedPost_by_id_should_return_likedPost(){
        //Act
        when(likedPostRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(likedPost));
        //Arrange
        LikedPost result = likedPostServiceImpl.getLikedPostById(1);
        //Assert
        Assert.assertEquals(likedPost, result);
    }

    @Test
    public void get_LikedPost_by_user_and_post_should_return_likedPost(){
        //Act
        when(likedPostRepository.findByUserAndPostAndEnabledTrue(any(), any())).thenReturn(Optional.of(likedPost));
        //Arrange
        LikedPost result = likedPostServiceImpl.getLikedPostByUserAndPost(user, post);
        //Assert
        Assert.assertEquals(likedPost, result);
    }

    @Test
    public void get_LikedPost_by_user_should_return_list_of_likedPost(){
        //Act
        when(likedPostRepository.findAllByUserAndEnabledTrue(any())).thenReturn(likedPosts);
        //Arrange
        List<LikedPost> result = likedPostServiceImpl.getLikedPostByUser(user);
        //Assert
        Assert.assertEquals(likedPosts.size(), result.size());
    }

    @Test
    public void get_likedPost_by_post_should_return_list_of_likedPost(){
        //Act
        when(likedPostRepository.findAllByPostAndEnabledTrue(any())).thenReturn(likedPosts);
        //Arrange
        List<LikedPost> result = likedPostServiceImpl.getLikedPostByPost(post);
        //Assert
        Assert.assertEquals(likedPosts.size(), result.size());
    }

    @Test
    public void get_count_likedPost_by_user_should_return_count_of_likedPost(){
        //Act
        when(likedPostRepository.countAllByUserAndEnabledTrue(any())).thenReturn(1);
        //Arrange
        Integer result = likedPostServiceImpl.getCountLikedPostByUser(user);
        //Assert
        Assert.assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void get_count_likedPost_by_post_should_return_count_of_likedPost(){
        //Act
        when(likedPostRepository.countAllByPostAndEnabledTrue(any())).thenReturn(1);
        //Arrange
        Integer result = likedPostServiceImpl.getCountLikedPostByPost(post);
        //Assert
        Assert.assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void test_does_likedPost_exist_should_return_boolean(){
        //Act
        when(likedPostRepository.existsByUserIdAndPostIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(true);
        //Arrange
        boolean result = likedPostServiceImpl.isLikeExist(1, 1);
        //Assert
        Assert.assertTrue(result);
    }

    @Test(expected = BadRequestException.class)
    public void should_throw_exception_create_likedPost_when_pass_invalid_post(){
        //Act
        when(likedPostRepository.existsByUserIdAndPostIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(false);
        when(likedPostRepository.findAll()).thenReturn(likedPosts);
        when(likedPost.getUser()).thenReturn(user);
        when(likedPost.getPost()).thenReturn(null);
        //Arrange
        likedPostServiceImpl.createLikedPost(likedPost);
    }

    @Test(expected = BadRequestException.class)
    public void should_throw_exception_create_likedPost_when_pass_invalid_user(){
        //Act
        when(likedPostRepository.existsByUserIdAndPostIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(false);
        when(likedPost.getUser()).thenReturn(null);
        when(likedPost.getPost()).thenReturn(post);
        //Arrange
        likedPostServiceImpl.createLikedPost(likedPost);
    }

    @Test(expected = BadRequestException.class)
    public void should_throw_exception_create_likedPost_when_likedPost_exists(){
        //Act
        when(likedPostRepository.existsByUserIdAndPostIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(true);
        when(likedPost.getUser()).thenReturn(user);
        when(likedPost.getPost()).thenReturn(post);
        //Arrange
        likedPostServiceImpl.createLikedPost(likedPost);
    }

    @Test
    public void test_create_likedPost_when_data_is_valid(){
        //Act
        when(likedPostRepository.existsByUserIdAndPostIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(false);
        when(likedPostRepository.findAll()).thenReturn(likedPosts);
        when(likedPost.getUser()).thenReturn(user);
        when(likedPost.getPost()).thenReturn(post);
        //Arrange
        likedPostServiceImpl.createLikedPost(likedPost);
        //Assert
        Assert.assertEquals(1, likedPostRepository.findAll().size());
    }

    @Test
    public void delete_likedPost(){
        //Act
        when(likedPostRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(likedPost));
        when(likedPostRepository.findAll()).thenReturn(likedPosts);
        likedPosts.remove(likedPost);
        //Arrange
        likedPostServiceImpl.deleteLikedPost(1);
        //Assert
        Assert.assertEquals(0, likedPostRepository.findAll().size());
    }
}
