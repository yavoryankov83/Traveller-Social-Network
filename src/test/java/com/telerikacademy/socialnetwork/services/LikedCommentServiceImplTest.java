package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.LikedComment;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.LikedCommentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class LikedCommentServiceImplTest {
    @Mock
    LikedCommentRepository likedCommentRepository;
    @InjectMocks
    LikedCommentServiceImpl likedCommentServiceImpl;

    private LikedComment likedComment;
    private User user;
    private Comment comment;
    private List<LikedComment>likedComments;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        likedComment = Mockito.mock(LikedComment.class);
        user = Mockito.mock(User.class);
        comment = Mockito.mock(Comment.class);
        likedComments = new ArrayList<>();
        likedComments.add(likedComment);
    }

    @Test(expected = NotFoundException.class)
    public void get_likedComment_by_id_when_data_invalid(){
        //Act
        when(likedCommentRepository.findByIdAndEnabledTrue(1)).thenReturn(Optional.empty());
        //Arrange
        LikedComment result = likedCommentServiceImpl.getLikedCommentById(1);
    }

    @Test
    public void get_likedComment_by_id_should_return_likedComment(){
        //Act
        when(likedCommentRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(likedComment));
        //Arrange
        LikedComment result = likedCommentServiceImpl.getLikedCommentById(1);
        //Assert
        Assert.assertEquals(likedComment, result);
    }

    @Test(expected = NotFoundException.class)
    public void get_likedComment_by_user_and_comment_when_data_invalid(){
        //Act
        when(likedCommentRepository.findByUserAndCommentAndEnabledTrue(any(), any())).thenReturn(Optional.empty());
        //Arrange
        LikedComment result = likedCommentServiceImpl.getLikedCommentByUserAndComment(user, comment);
    }

    @Test
    public void get_likedComment_by_user_and_comment_should_return_likedComment(){
        //Act
        when(likedCommentRepository.findByUserAndCommentAndEnabledTrue(any(), any())).thenReturn(Optional.of(likedComment));
        //Arrange
        LikedComment result = likedCommentServiceImpl.getLikedCommentByUserAndComment(user, comment);
        //Assert
        Assert.assertEquals(likedComment, result);
    }

    @Test
    public void get_likedComments_by_user_should_return_list_of_likedComments(){
        //Act
        when(likedCommentRepository.findAllByUserAndEnabledTrue(any())).thenReturn(likedComments);
        //Arrange
        List<LikedComment> result = likedCommentServiceImpl.getLikedCommentsByUser(user);
        //Assert
        Assert.assertEquals(likedComments.size(), result.size());
    }

    @Test
    public void get_likedComment_by_comment_should_return_list_of_likedComments(){
        //Act
        when(likedCommentRepository.findAllByCommentAndEnabledTrue(any())).thenReturn(likedComments);
        //Arrange
        List<LikedComment> result = likedCommentServiceImpl.getLikedCommentByComment(comment);
        //Assert
        Assert.assertEquals(likedComments.size(), result.size());
    }

    @Test
    public void get_count_likedComment_by_user_should_return_count_of_likedComments(){
        //Act
        when(likedCommentRepository.countAllByUserAndEnabledTrue(any())).thenReturn(1);
        //Arrange
        Integer result = likedCommentServiceImpl.getCountLikedCommentByUser(user);
        //Assert
        Assert.assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void get_count_likedComment_by_comment_should_return_count_of_likedComments(){
        //Act
        when(likedCommentRepository.countAllByComment(any())).thenReturn(1);
        //Arrange
        Integer result = likedCommentServiceImpl.getCountLikedCommentByComment(comment);
        //Assert
        Assert.assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void test_does_likedComment_exist_should_return_boolean(){
        //Act
        when(likedCommentRepository.existsByUserIdAndCommentIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(true);
        //Arrange
        boolean result = likedCommentServiceImpl.isLikeExist(1, 1);
        //Assert
        Assert.assertTrue(result);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_to_create_likedComment_when_user_invalid(){
        //Act
        when(likedCommentRepository.existsByUserIdAndCommentIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(false);
        when(likedComment.getUser()).thenReturn(null);
        when(likedComment.getComment()).thenReturn(comment);
        //Arrange
        likedCommentServiceImpl.createLikedComment(likedComment);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_to_create_likedComment_when_comment_invalid(){
        //Act
        when(likedCommentRepository.existsByUserIdAndCommentIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(false);
        when(likedComment.getUser()).thenReturn(user);
        when(likedComment.getComment()).thenReturn(null);
        //Arrange
        likedCommentServiceImpl.createLikedComment(likedComment);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_to_create_likedComment_when_likedComment_invalid(){
        //Act
        when(likedCommentRepository.existsByUserIdAndCommentIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(true);
        when(likedComment.getUser()).thenReturn(user);
        when(likedComment.getComment()).thenReturn(comment);
        //Arrange
        likedCommentServiceImpl.createLikedComment(likedComment);
    }

    @Test
    public void create_likedComment_when_data_valid(){
        //Act
        when(likedCommentRepository.existsByUserIdAndCommentIdAndEnabledTrue(anyInt(), anyInt())).thenReturn(false);
        when(likedCommentRepository.findAll()).thenReturn(likedComments);
        when(likedComment.getUser()).thenReturn(user);
        when(likedComment.getComment()).thenReturn(comment);
        //Arrange
        likedCommentServiceImpl.createLikedComment(likedComment);
        //Assert
        Assert.assertEquals(1, likedCommentRepository.findAll().size());
    }

    @Test
    public void delete_likedComment_when_data_valid(){
        //Act
        when(likedCommentRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(likedComment));
        likedComments.remove(likedComment);
        //Arrange
        likedCommentServiceImpl.deleteLikedComment(1);
        //Assert
        Assert.assertEquals(0, likedCommentRepository.findAll().size());
    }
}
