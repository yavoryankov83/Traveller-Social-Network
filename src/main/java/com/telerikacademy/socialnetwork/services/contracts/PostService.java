package com.telerikacademy.socialnetwork.services.contracts;

import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.PostDTO;
import com.telerikacademy.socialnetwork.models.User;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

public interface PostService {

  List<Post> getAllPosts();

  List<Post> getAllPublicPosts();

  List<Post> getAllPublicPosts(Integer page, Integer size);

  Post getPostById(Integer postId);

  List<PostDTO> getAllPublicPostsDTO();

  List<Post> getPostsByUser(User user);

  List<Post> getPostsByUser(User user, Integer page, Integer size);

  List<Post> filterPostsByUsernameAndEmailAndFirstNameAndLastName(String filterParam);

  List<Post> getAllPostsVisibleByPrincipal(Principal principal);

  List<Post> getAllPostsVisibleByPrincipal(Principal principal, Integer countPerPage);

  @Transactional
  void createPost(Post post);

  @Transactional
  void updatePost(Integer postId, Post post);

  @Transactional
  void deletePost(Integer postId);
}
