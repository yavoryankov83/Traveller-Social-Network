package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.models.Comment;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.CommentRepository;
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

public class CommentServiceImplTest {
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    CommentServiceImpl commentServiceImpl;

    private Comment comment;
    private Comment parent;
    private Comment child;
    private User user;
    private Post post;
    private List<Comment>comments;
    private List<Comment>parents;
    private List<Comment>childComments;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        comment = Mockito.mock(Comment.class);
        parent = Mockito.mock(Comment.class);
        child = Mockito.mock(Comment.class);
        post = Mockito.mock(Post.class);
        user = Mockito.mock(User.class);
        comments = new ArrayList<>();
        parents = new ArrayList<>();
        childComments = new ArrayList<>();
        comments.add(comment);
        parents.add(parent);
        childComments.add(child);
    }

    @Test
    public void get_comment_by_id_should_return_comment(){
        //Act
        when(commentRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(comment));
        //Arrange
        Comment result = commentServiceImpl.getCommentById(1);
        //Assert
        Assert.assertEquals(comment, result);
    }

    @Test
    public void get_all_comments_by_user_and_post_should_return_list_comments(){
        //Act
        when(commentRepository.findAllByUserAndPostAndEnabledTrueOrderByUpdateDateDesc(any(), any())).thenReturn(comments);
        //Arrange
        List<Comment> result = commentServiceImpl.getAllCommentsByUserAndPost(user, post);
        //Assert
        Assert.assertEquals(comments.size(), result.size());
    }

    @Test
    public void get_all_comments_by_post_should_return_list_comments(){
        //Act
        when(commentRepository.findByPostAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(comments);
        //Arrange
        List<Comment> result = commentServiceImpl.getAllCommentsByPost(post);
        //Assert
        Assert.assertEquals(comments.size(), result.size());
    }

    @Test
    public void get_all_parent_comments_by_post_should_return_list_comments(){
        //Act
        when(commentRepository.findByPostAndEnabledTrueAndParentCommentIsNullOrderByUpdateDateDesc(any())).thenReturn(comments);
        //Arrange
        List<Comment> result = commentServiceImpl.getAllParentCommentsByPost(post);
        //Assert
        Assert.assertEquals(comments.size(), result.size());
    }

    @Test
    public void get_all_parent_comments_by_post_overload_should_return_list_comments(){
        //Act
        when(commentRepository.findByPostAndEnabledTrueAndParentCommentIsNullOrderByUpdateDateDesc(any(), any())).thenReturn(comments);
        //Arrange
        List<Comment> result = commentServiceImpl.getAllParentCommentsByPost(post, 1, 1);
        //Assert
        Assert.assertEquals(comments.size(), result.size());
    }

    @Test
    public void get_all_child_comments_by_parent_comment_should_return_list_comments(){
        //Arrange
        List<Comment> result = commentServiceImpl.getAllChildCommentsByParentComment(comment);
        result.add(comment);
        //Assert
        Assert.assertEquals(childComments.size(), result.size());
    }

    @Test(expected = BadRequestException.class)
    public void should_throw_exception_create_comment_when_content_invalid(){
        //Act
        when(commentRepository.findAll()).thenReturn(comments);
        when(comment.getUser()).thenReturn(user);
        when(comment.getPost()).thenReturn(post);
        when(comment.getContent()).thenReturn(null);
        when(comment.getParentComment()).thenReturn(parent);
        //Arrange
        commentServiceImpl.createComment(comment);
    }

    @Test(expected = BadRequestException.class)
    public void should_throw_exception_create_comment_when_post_invalid(){
        //Act
        when(commentRepository.findAll()).thenReturn(comments);
        when(comment.getUser()).thenReturn(user);
        when(comment.getPost()).thenReturn(null);
        when(comment.getContent()).thenReturn("content");
        when(comment.getParentComment()).thenReturn(parent);
        //Arrange
        commentServiceImpl.createComment(comment);
    }

    @Test(expected = BadRequestException.class)
    public void should_throw_exception_create_comment_when_user_invalid(){
        //Act
        when(commentRepository.findAll()).thenReturn(comments);
        when(comment.getUser()).thenReturn(null);
        when(comment.getPost()).thenReturn(post);
        when(comment.getContent()).thenReturn("content");
        when(comment.getParentComment()).thenReturn(parent);
        //Arrange
        commentServiceImpl.createComment(comment);
    }

    @Test
    public void create_comment_when_data_valid(){
        //Act
        when(commentRepository.findAll()).thenReturn(comments);
        when(comment.getUser()).thenReturn(user);
        when(comment.getPost()).thenReturn(post);
        when(comment.getContent()).thenReturn("content");
        when(comment.getParentComment()).thenReturn(parent);
        //Arrange
        commentServiceImpl.createComment(comment);
        //Assert
        Assert.assertEquals(1, commentRepository.findAll().size());
    }

    @Test
    public void update_comment_when_data_valid(){
        //Act
        when(commentRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(comment));
        when(commentRepository.findAll()).thenReturn(comments);
        when(comment.getContent()).thenReturn("content");
        //Arrange
        commentServiceImpl.updateComment(1, comment);
        //Assert
        Assert.assertEquals(1, commentRepository.findAll().size());
    }

    @Test
    public void delete_comment_when_data_valid_with_parent(){
        //Act
        when(commentRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(comment));
        when(commentRepository.findAllByParentCommentIdAndEnabledTrueOrderByUpdateDateDesc(anyInt())).thenReturn(childComments);
        when(comment.getParentComment()).thenReturn(parent);
        //Arrange
        commentServiceImpl.deleteComment(1);
        //Assert
        Assert.assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    public void delete_comment_when_data_valid_without_parent(){
        //Act
        Comment comment = new Comment();
        Post post = new Post();
        comment.setPost(post);
        post.setComments(comments);
        List<Comment>childList = new ArrayList<>();
        childList.add(child);
        comment.setComments(childList);
        when(commentRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(comment));
        when(commentRepository.findAllByParentCommentIdAndEnabledTrueOrderByUpdateDateDesc(anyInt())).thenReturn(childList);
        //Arrange
        commentServiceImpl.deleteComment(1);
        //Assert
        Assert.assertEquals(0, commentRepository.findAll().size());
    }
}