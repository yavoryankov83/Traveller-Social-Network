package com.telerikacademy.socialnetwork.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@MappedSuperclass
@ApiModel(description = "All common details about the LikedPost/LikedComment")
public abstract class AbstractLikeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ApiModelProperty(notes = "The database generated user ID")
  private Integer id;

  @Column(name = "enabled")
  @ApiModelProperty(notes = "The LikedPost/LikedComment status active/not active")
  private boolean enabled = true;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ApiModelProperty(notes = "The LikedPost/LikedComment user")
  private User user;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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
}
