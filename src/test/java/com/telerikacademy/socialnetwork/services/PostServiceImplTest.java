package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.ConflictException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.models.*;
import com.telerikacademy.socialnetwork.repositories.CommentRepository;
import com.telerikacademy.socialnetwork.repositories.LikedPostRepository;
import com.telerikacademy.socialnetwork.repositories.PostRepository;
import com.telerikacademy.socialnetwork.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;
import java.security.Principal;
import java.util.*;

import static org.mockito.Mockito.*;

public class PostServiceImplTest{
    @Mock
    PostRepository postRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    LikedPostRepository likedPostRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    PostServiceImpl postServiceImpl;

    private Post post;
    private Post post1;
    private User user;
    private Principal principal;
    private AbstractTimestampEntity abstractTimestampEntity;
    private List <Post> postList;
    private List<PostDTO> postDTOList;
    private List<Comment> comments;
    private List<LikedPost> postsLikes;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        post = Mockito.mock(Post.class);
        post1 = new Post();
        PostDTO postDTO = Mockito.mock(PostDTO.class);
        Comment comment = Mockito.mock(Comment.class);
        abstractTimestampEntity = Mockito.mock(AbstractTimestampEntity.class);
        LikedPost likedPost = Mockito.mock(LikedPost.class);
//        comparator = Comparator.comparing(AbstractTimestampEntity::getUpdateDate);
        user = Mockito.mock(User.class);
        principal = Mockito.mock(Principal.class);
        postList = new ArrayList<>();
        postList.add(post);
        postDTOList = new ArrayList<>();
        postDTOList.add(postDTO);
        comments = new ArrayList<>();
        comments.add(comment);
        postsLikes = new ArrayList<>();
        postsLikes.add(likedPost);
    }

    @Test
    public void get_all_posts_should_return_list_of_posts(){
        //Act
        when(postRepository.findAllByEnabledTrueOrderByUpdateDateDesc()).thenReturn(postList);
        //Arrange
        List<Post> result = postServiceImpl.getAllPosts();
        //Assert
        Assert.assertEquals(postList.size(), result.size());
    }

    @Test
    public void get_all_public_posts_should_return_list_public_posts(){
        //Act
        when(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc()).thenReturn(postList);
        //Arrange
        List<Post> result = postServiceImpl.getAllPublicPosts();
        //Assert
        Assert.assertEquals(postList.size(), result.size());
    }

    @Test
    public void get_all_public_posts_should_return_list_public_posts_overload(){
        //Act
        when(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc(any())).thenReturn(postList);
        //Arrange
        List<Post> result = postServiceImpl.getAllPublicPosts(1, 1);
        //Assert
        Assert.assertEquals(postList.size(), result.size());
    }

    @Test(expected = NotFoundException.class)
    public void get_post_by_id_should_return_exception() {
        //Arrange
        when(postRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.empty());
        //Act
        Post result = postServiceImpl.getPostById(1);
    }

    @Test(expected = NotFoundException.class)
    public void get_post_by_id_when_post_not_exists(){
        //Act
        when(postRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.empty());
        //Arrange
        Post result = postServiceImpl.getPostById(1);
    }

    @Test
    public void get_post_by_id_should_return_post(){
        //Act
        when(postRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.ofNullable(post));
        //Arrange
        Post result = postServiceImpl.getPostById(1);
        //Assert
        Assert.assertEquals(post, result);
    }

    @Test
    public void get_all_public_postsDTO_return_list_postDTO(){
        //Act
        when(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc()).thenReturn(postList);
        //Arrange
        List<PostDTO> result = postServiceImpl.getAllPublicPostsDTO();
        //Assert
        Assert.assertEquals(postDTOList.size(), result.size());
    }

    @Test
    public void get_posts_by_user_should_return_list_of_post(){
        //Act
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(postList);
        //Arrange
        List<Post> result = postServiceImpl.getPostsByUser(new User());
        //Assert
        Assert.assertEquals(postList.size(), result.size());
    }

    @Test
    public void get_posts_by_user_overload_should_return_list_of_post(){
        //Act
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any(), any())).thenReturn(postList);
        //Arrange
        List<Post> result = postServiceImpl.getPostsByUser(user, 1, 1);
        //Assert
        Assert.assertEquals(postList.size(), result.size());
    }

    @Test
    public void filter_posts_by_username_email_firstName_lastName(){
        //Act
        List<Post>posts = new ArrayList<>();
        when(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc()).thenReturn(posts);
        User user = new User();
        Post post2 = new Post();
        post2.setUser(user);
        user.setUsername("username");
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("email");
        posts.add(post2);
        //Arrange
        List<Post> result = postServiceImpl.filterPostsByUsernameAndEmailAndFirstNameAndLastName("email");
        //Assert
        Assert.assertEquals(postList.size(), result.size());
    }

    @Test(expected = NotFoundException.class)
    public void get_all_posts_visible_by_principal_null_should_throw_exception(){
        //Act
        when(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc()).thenReturn(postList);
        when(postRepository.findAllByUserAndEnabledTrueAndVisibleFalse(any())).thenReturn(postList);
        when(postRepository.findAllPrivatePostsOfFriendShipsRequestedByPrincipal(anyInt())).thenReturn(postList);
        when(postRepository.findAllPrivatePostsOfFriendShipsAcceptedByPrincipal(anyInt())).thenReturn(postList);
        when(principal.getName()).thenReturn("principal");
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.empty());
        //Arrange
        List<Post> result = postServiceImpl.getAllPostsVisibleByPrincipal(principal);
    }

    @Test
    public void get_all_posts_visible_by_principal_should_return_list_of_post(){
        //Act
        Post post = new Post();
        Date date = new Date();
        post.setUpdateDate(date);
        List<Post>posts = new ArrayList<>();
        posts.add(post);
        when(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc()).thenReturn(posts);
        when(postRepository.findAllByUserAndEnabledTrueAndVisibleFalse(any())).thenReturn(posts);
        when(postRepository.findAllPrivatePostsOfFriendShipsRequestedByPrincipal(anyInt())).thenReturn(posts);
        when(postRepository.findAllPrivatePostsOfFriendShipsAcceptedByPrincipal(anyInt())).thenReturn(posts);
        when(principal.getName()).thenReturn("principal");
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        //Arrange
        List<Post> result = postServiceImpl.getAllPostsVisibleByPrincipal(principal);
        //Assert
        Assert.assertEquals(posts.get(0), result.get(0));
    }

    @Test
    public void get_all_posts_visible_by_principal_should_return_list_of_post_overload(){
        //Act
        Post post = new Post();
        Date date = new Date();
        post.setUpdateDate(date);
        List<Post>posts = new ArrayList<>();
        posts.add(post);
        when(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc()).thenReturn(posts);
        when(postRepository.findAllByUserAndEnabledTrueAndVisibleFalse(any())).thenReturn(posts);
        when(postRepository.findAllPrivatePostsOfFriendShipsRequestedByPrincipal(anyInt())).thenReturn(posts);
        when(postRepository.findAllPrivatePostsOfFriendShipsAcceptedByPrincipal(anyInt())).thenReturn(posts);
        when(userRepository.findByUsernameAndEnabledTrue(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("principal");
        //Arrange
        List<Post> result = postServiceImpl.getAllPostsVisibleByPrincipal(principal, 1);
        //Assert
        Assert.assertEquals(posts.get(0), result.get(0));
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_create_post_with_invalid_user(){
        //Act
        post1.setUser(null);
        post1.setContent("content");
        post1.setVideo("");
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(postList);
        when(postRepository.findAll()).thenReturn(postList);
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(user)).thenReturn(postList);
        when(post.getContent()).thenReturn("string");
        //Arrange
        postServiceImpl.createPost(post1);
    }

    @Test(expected = BadRequestException.class)
    public void throw_exception_create_post_with_invalid_content(){
        //Act
        post1.setUser(user);
        post1.setContent("");
        post1.setVideo("");
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(postList);
        when(postRepository.findAll()).thenReturn(postList);
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(user)).thenReturn(postList);
        when(post.getContent()).thenReturn("string");
        //Arrange
        postServiceImpl.createPost(post1);
    }

    @Test(expected = ConflictException.class)
    public void throw_exception_create_post_when_post_exists(){
        //Act
        post1.setUser(user);
        post1.setContent("content");
        post1.setVideo("youtube=video");
        post1.setPostPhoto(new byte[2]);
        postList.add(post1);
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(postList);
        when(postRepository.findAll()).thenReturn(postList);
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(user)).thenReturn(postList);
        when(post.getContent()).thenReturn("string");
        //Arrange
        postServiceImpl.createPost(post1);
    }

    @Test
    public void create_post_when_data_valid(){
        //Act
        post1.setUser(user);
        post1.setContent("content");
        post1.setVideo("youtube=video");
        post1.setPostPhoto(new byte[2]);
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(postList);
        when(postRepository.findAll()).thenReturn(postList);
        when(postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(user)).thenReturn(postList);
        when(post.getContent()).thenReturn("string");
        //Arrange
        postServiceImpl.createPost(post1);
        //Assert
        Assert.assertEquals(1, postRepository.findAll().size());
    }

    @Test(expected = ConflictException.class)
    public void throw_exception_update_post_when_post_invalid(){
        //Act
        when(postRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(post));
        when(post.getContent()).thenReturn("content");
        when(postRepository.existsByContentAndEnabledTrueAndIdNotLike(anyString(), anyInt())).thenReturn(true);
        when(post.getVideo()).thenReturn("");
        //Arrange
        postServiceImpl.updatePost(1, post);
    }

    @Test
    public void update_post_when_data_valid(){
        //Act
        when(postRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(post));
        when(postRepository.existsByContentAndEnabledTrueAndIdNotLike(anyString(), anyInt())).thenReturn(false);
        when(postRepository.findAll()).thenReturn(postList);
        when(post.getVideo()).thenReturn("");
        //Arrange
        postServiceImpl.updatePost(1, post);
        //Assert
        Assert.assertEquals(1, postRepository.findAll().size());
    }

    @Test
    public void delete_post_when_data_valid(){
        //Act
        when(postRepository.findByIdAndEnabledTrue(anyInt())).thenReturn(Optional.of(post));
        when(commentRepository.findByPostAndEnabledTrueOrderByUpdateDateDesc(any())).thenReturn(comments);
        when(postRepository.findAll()).thenReturn(postList);
        when(likedPostRepository.findAllByPostAndEnabledTrue(any())).thenReturn(postsLikes);
        //Arrange
        postServiceImpl.deletePost(1);
        //Assert
        Assert.assertEquals(1, postRepository.findAll().size());
    }
}
