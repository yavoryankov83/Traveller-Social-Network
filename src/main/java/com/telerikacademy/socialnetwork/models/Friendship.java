package com.telerikacademy.socialnetwork.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "friendships")
@ApiModel(description = "All details about the Friendship")
public class Friendship extends AbstractTimestampEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ApiModelProperty(notes = "The database generated user ID")
  private Integer id;

  @Column(name = "friendship_status_id")
  @ApiModelProperty(notes = "The Friendship status")
  private Integer statusId = 1;

  @Column(name = "enabled")
  @ApiModelProperty(notes = "The Friendship status active/not active")
  private boolean enabled = true;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ApiModelProperty(notes = "The user who send friendship request")
  private User user;

  @ManyToOne
  @JoinColumn(name = "friend_id")
  @ApiModelProperty(notes = "The user to whom was send friendship request")
  private User friend;

  public Friendship() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getStatusId() {
    return statusId;
  }

  public void setStatusId(Integer statusId) {
    this.statusId = statusId;
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

  public User getFriend() {
    return friend;
  }

  public void setFriend(User friend) {
    this.friend = friend;
  }
}
