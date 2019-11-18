package com.telerikacademy.socialnetwork.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "liked_posts")
@ApiModel(description = "All details about the LikedPost")
public class LikedPost extends AbstractLikeEntity implements Serializable {

  @ManyToOne
  @JoinColumn(name = "post_id")
  @ApiModelProperty(notes = "The LikedPost")
  private Post post;

  public LikedPost() {
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }
}
