package com.telerikacademy.socialnetwork.services;

import com.telerikacademy.socialnetwork.exceptions.BadRequestException;
import com.telerikacademy.socialnetwork.exceptions.ConflictException;
import com.telerikacademy.socialnetwork.exceptions.NotFoundException;
import com.telerikacademy.socialnetwork.helper.Constants;
import com.telerikacademy.socialnetwork.helper.Helper;
import com.telerikacademy.socialnetwork.models.AbstractTimestampEntity;
import com.telerikacademy.socialnetwork.models.Post;
import com.telerikacademy.socialnetwork.models.PostDTO;
import com.telerikacademy.socialnetwork.models.User;
import com.telerikacademy.socialnetwork.repositories.CommentRepository;
import com.telerikacademy.socialnetwork.repositories.LikedPostRepository;
import com.telerikacademy.socialnetwork.repositories.PostRepository;
import com.telerikacademy.socialnetwork.repositories.UserRepository;
import com.telerikacademy.socialnetwork.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

  private PostRepository postRepository;
  private CommentRepository commentRepository;
  private LikedPostRepository likedPostRepository;
  private UserRepository userRepository;

  @Autowired
  public PostServiceImpl(PostRepository postRepository,
                         CommentRepository commentRepository,
                         LikedPostRepository likedPostRepository,
                         UserRepository userRepository) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.likedPostRepository = likedPostRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<Post> getAllPosts() {
    return postRepository.findAllByEnabledTrueOrderByUpdateDateDesc();
  }

  @Override
  public List<Post> getAllPublicPosts() {
    return postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc();
  }

  @Override
  public List<Post> getAllPublicPosts(Integer page, Integer size) {
    return postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc(PageRequest.of(page, size));
  }

  @Override
  public Post getPostById(Integer postId) {
    return postRepository.findByIdAndEnabledTrue(postId)
            .orElseThrow(() -> new NotFoundException(Constants.POST_NOT_FOUND_MESSAGE));
  }

  @Override
  public List<PostDTO> getAllPublicPostsDTO() {
    List<PostDTO> postDTOS = new ArrayList<>();

    postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc()
            .forEach(post -> {
              PostDTO postDTO = new PostDTO();
              postDTO.setContent(post.getContent());
              postDTO.setId(post.getId());
              postDTOS.add(postDTO);
            });

    return postDTOS;
  }

  @Override
  public List<Post> getPostsByUser(User user) {
    return postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(user);
  }

  @Override
  public List<Post> getPostsByUser(User user, Integer page, Integer size) {
    return postRepository.findAllByUserAndEnabledTrueOrderByUpdateDateDesc(user, Helper.createPageRequest(page, size));
  }

  @Override
  public List<Post> filterPostsByUsernameAndEmailAndFirstNameAndLastName(String filterParam) {
    List<Post> filteredPosts = new ArrayList<>();

    getAllPublicPosts().forEach(post -> getFilteredPosts(filterParam, filteredPosts, post));
    return filteredPosts;
  }

  @Override
  public List<Post> getAllPostsVisibleByPrincipal(Principal principal) {
    List<Post> posts = getPostsOnNewsfeed(principal);

    return posts;
  }

  @Override
  public List<Post> getAllPostsVisibleByPrincipal(Principal principal, Integer countPerPage) {
    List<Post> posts = getPostsOnNewsfeed(principal);

    return posts.subList(0, countPerPage);
  }

  @Override
  @Transactional
  public void createPost(Post post) {
    checkUserValidity(post);
    checkContentValidity(post);
    checkPostIsExist(post);

    Post postToCreate = new Post();

    postToCreate.setContent(post.getContent());
    postToCreate.setUser(post.getUser());
    checkPhotoValidity(post, postToCreate);
    checkVideoValidity(post, postToCreate);
    checkLocationValidity(post, postToCreate);
    postToCreate.setVisible(post.getVisible());
    postToCreate.setEnabled(post.getEnabled());

    postRepository.save(postToCreate);
  }

  @Override
  @Transactional
  public void updatePost(Integer postId, Post post) {
    checkContentValidity(post);
    checkPostIsExist(postId, post);
    Post postToUpdate = getPostById(postId);
    postToUpdate.setContent(post.getContent());
    checkPhotoValidity(post, postToUpdate);
    checkVideoValidity(post, postToUpdate);
    checkLocationValidity(post, postToUpdate);
    postToUpdate.setVisible(post.getVisible());

    postRepository.save(postToUpdate);
  }

  @Override
  @Transactional
  public void deletePost(Integer postId) {
    Post postToDelete = getPostById(postId);

    deletePostComments(postToDelete);

    deleteLikedPosts(postToDelete);

    postToDelete.setEnabled(false);

    postRepository.save(postToDelete);
  }

  private void deleteLikedPosts(Post postToDelete) {
    likedPostRepository.findAllByPostAndEnabledTrue(postToDelete)
            .forEach(likedPost -> {
              likedPost.setEnabled(false);
              likedPostRepository.save(likedPost);
            });
  }

  private void deletePostComments(Post postToDelete) {
    commentRepository.findByPostAndEnabledTrueOrderByUpdateDateDesc(postToDelete)
            .forEach(comment -> {
              comment.setEnabled(false);
              commentRepository.save(comment);
            });
  }

  //using for create
  private void checkPostIsExist(Post post) {
    User userToPost = post.getUser();
    boolean isPostExist = getPostsByUser(userToPost)
            .stream()
            .anyMatch(post1 -> post1.getContent()
                    .equalsIgnoreCase(post.getContent()));
    if (isPostExist) {
      throw new ConflictException(Constants.POST_ALREADY_EXISTS);
    }
  }

  //using for update
  private void checkPostIsExist(Integer postId, Post post) {
    if (postRepository.existsByContentAndEnabledTrueAndIdNotLike(post.getContent(), postId)) {
      throw new ConflictException(Constants.POST_ALREADY_EXISTS);
    }
  }

  private void checkUserValidity(Post post) {
    if (post.getUser() == null) {
      throw new BadRequestException(Constants.USER_NOT_FOUND_MESSAGE);
    }
  }

  private void checkContentValidity(Post post) {
    if (Constants.EMPTY_STRING.equals(post.getContent())) {
      throw new BadRequestException(Constants.POST_CONTENT_EMPTY_MESSAGE);
    }
  }

  private void checkLocationValidity(Post post, Post postToUpdate) {
    if (!Constants.EMPTY_STRING.equals(post.getLocation())) {
      postToUpdate.setLocation(post.getLocation());
    }
  }

  private void checkPhotoValidity(Post post, Post postToCreate) {
    if (post.getPostPhoto() != null) {
      postToCreate.setPostPhoto(post.getPostPhoto());
    }
  }

  private void checkVideoValidity(Post post, Post postToUpdate) {
    if (!Constants.EMPTY_STRING.equals(post.getVideo()) && post.getVideo() != null) {
      String inputVideo = post.getVideo();
      int index = inputVideo.lastIndexOf("=");

      if (index != -1) {
        String videoAddress = inputVideo.substring(index + 1);
        String result = "https://www.youtube.com/embed/" + videoAddress;
        postToUpdate.setVideo(result);
      }
    }
  }

  private User getPrincipal(Principal principal) {
    String principalName = principal.getName();

    Optional<User> currentPrincipal = userRepository.findByUsernameAndEnabledTrue(principalName);
    if (!currentPrincipal.isPresent()) {
      throw new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE);
    }
    return currentPrincipal.get();
  }

  private void getFilteredPosts(String filterParam, List<Post> filteredPosts, Post post) {
    if (post.getUser().getUsername().startsWith(filterParam.toLowerCase()) ||
            post.getUser().getFirstName().startsWith(filterParam.toLowerCase()) ||
            post.getUser().getLastName().startsWith(filterParam.toLowerCase()) ||
            post.getUser().getEmail().startsWith(filterParam.toLowerCase())) {
      filteredPosts.add(post);
    }
  }

  private List<Post> getPostsOnNewsfeed(Principal principal) {
    User userPrincipal = getPrincipal(principal);
    Integer principalId = userPrincipal.getId();

    List<Post> posts = new ArrayList<>();
    posts.addAll(postRepository.findAllPrivatePostsOfFriendShipsAcceptedByPrincipal(principalId));
    posts.addAll(postRepository.findAllPrivatePostsOfFriendShipsRequestedByPrincipal(principalId));
    posts.addAll(postRepository.findAllByUserAndEnabledTrueAndVisibleFalse(userPrincipal));
    posts.addAll(postRepository.findAllByEnabledTrueAndVisibleTrueOrderByUpdateDateDesc());
    posts.sort(Comparator.comparing(AbstractTimestampEntity::getUpdateDate).reversed());
    return posts;
  }
}
