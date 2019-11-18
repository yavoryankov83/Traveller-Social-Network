package com.telerikacademy.socialnetwork.models;

import java.util.Date;

public class PostDTO {

  private Integer id;

  private String content;

  private String photo;

  private Date create;

  private Date update;

  private Boolean visibility;

  private String location;

  private User user;

  public PostDTO() {
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

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public Date getCreate() {
    return create;
  }

  public void setCreate(Date create) {
    this.create = create;
  }

  public Date getUpdate() {
    return update;
  }

  public void setUpdate(Date update) {
    this.update = update;
  }

  public Boolean getVisibility() {
    return visibility;
  }

  public void setVisibility(Boolean visibility) {
    this.visibility = visibility;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
