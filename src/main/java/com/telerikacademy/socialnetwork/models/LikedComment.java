package com.telerikacademy.socialnetwork.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "liked_comments")
@ApiModel(description = "All details about the LikedComment")
public class LikedComment extends AbstractLikeEntity implements Serializable {

  @ManyToOne
  @JoinColumn(name = "comment_id")
  @ApiModelProperty(notes = "The LikedComment")
  private Comment comment;

  public LikedComment() {
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
