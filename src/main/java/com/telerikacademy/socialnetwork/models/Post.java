package com.telerikacademy.socialnetwork.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.socialnetwork.helper.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "posts")
@ApiModel(description = "All details about the Post")
public class Post extends AbstractTimestampEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ApiModelProperty(notes = "The database generated user ID")
  private Integer id;

  @Size(min = 3, max = 255, message = Constants.CONTENT_ERROR_MESSAGE)
  @Column(name = "content", nullable = false)
  @ApiModelProperty(notes = "The Post content")
  private String content;

  @Nullable
  @Lob
  @Column(name = "photo")
  @Basic(fetch = FetchType.LAZY)
  @ApiModelProperty(notes = "The Post photo")
  private byte[] postPhoto;

  @Nullable
  @Column(name = "video", nullable = false)
  @ApiModelProperty(notes = "The Post video")
  private String video;

  @Nullable
  @Size(max = 255, message = Constants.LOCATION_ERROR_MESSAGE)
  @Column(name = "location")
  @ApiModelProperty(notes = "The Post location")
  private String location;

  @Column(name = "visibility", nullable = false)
  @ApiModelProperty(notes = "The Post visibility")
  private Boolean visible = true;

  @Column(name = "enabled", nullable = false)
  @ApiModelProperty(notes = "The Post status active/not active")
  private Boolean enabled = true;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ApiModelProperty(notes = "The Post user")
  private User user;

  @JsonIgnore
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "post")
  private List<Comment> comments;

  @JsonIgnore
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "post")
  private List<LikedPost> postsLikes;

  public Post() {
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

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public byte[] getPostPhoto() {
    return postPhoto;
  }

  public String getPostPhotoAsString(){
    if (postPhoto != null) {
      return "data:image/png;base64," + Base64.getEncoder().encodeToString(postPhoto);
    } else {
      return Constants.POST_DEFAULT_PHOTO;
    }
  }

  public void setPostPhoto(byte[] postPhoto) {
    this.postPhoto = postPhoto;
  }

  public String getVideo() {
    return video;
  }

  public void setVideo(String video) {
    this.video = video;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public List<LikedPost> getPostsLikes() {
    return postsLikes;
  }

  public void setPostsLikes(List<LikedPost> postsLikes) {
    this.postsLikes = postsLikes;
  }
}