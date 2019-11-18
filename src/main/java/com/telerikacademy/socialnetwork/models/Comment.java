package com.telerikacademy.socialnetwork.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.socialnetwork.helper.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "comments")
@ApiModel(description = "All details about the Comment")
public class Comment extends AbstractTimestampEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ApiModelProperty(notes = "The database generated comment ID")
  private Integer id;

  @Size(min = 3, max = 255, message = Constants.CONTENT_ERROR_MESSAGE)
  @Column(name = "content")
  @ApiModelProperty(notes = "The Comment content")
  private String content;

  @Column(name = "enabled", nullable = false)
  @ApiModelProperty(notes = "The Comment status active/not active")
  private boolean enabled = true;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ApiModelProperty(notes = "The Comment user")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id")
  @ApiModelProperty(notes = "The Post to comment")
  private Post post;

  @JsonIgnore
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "comment")
  private List<LikedComment> commentLikes;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Comment parentComment;

  @JsonIgnore
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "parentComment")
  private List<Comment> comments;

  public Comment() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public Comment getParentComment() {
    return parentComment;
  }

  public void setParentComment(Comment parentComment) {
    this.parentComment = parentComment;
  }

  public List<LikedComment> getCommentLikes() {
    return commentLikes;
  }

  public void setCommentLikes(List<LikedComment> commentLikes) {
    this.commentLikes = commentLikes;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }
}
