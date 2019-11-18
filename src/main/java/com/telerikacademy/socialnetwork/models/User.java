package com.telerikacademy.socialnetwork.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.socialnetwork.helper.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name = "users")
@ApiModel(description = "All details about the User")
public class User extends AbstractTimestampEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ApiModelProperty(notes = "The database generated user ID")
  private Integer id;

  @Size(min = 3, max = 50, message = Constants.USERNAME_ERROR_MESSAGE)
  @Column(name = "username", unique = true, nullable = false)
  @ApiModelProperty(notes = "The User username")
  private String username;

  @Size(min = 3, max = 68, message = Constants.PASSWORD_ERROR_MESSAGE)
  @Column(name = "password", nullable = false)
  @ApiModelProperty(notes = "The User password")
  private String password;

  @Size(min = 3, max = 68, message = Constants.PASSWORD_ERROR_MESSAGE)
  @Transient
  @ApiModelProperty(notes = "The User password confirmation")
  private String passwordConfirmation;

  @Size(min = 3, max = 50)
  @Email(regexp = Constants.EMAIL_VALIDATION_REGEX, message = Constants.EMAIL_ERROR_MESSAGE)
  @Column(name = "email", nullable = false, unique = true)
  @ApiModelProperty(notes = "The User email")
  private String email;

  @Size(min = 3, max = 50, message = Constants.FIRST_NAME_ERROR_MESSAGE)
  @Column(name = "first_name")
  @ApiModelProperty(notes = "The User first name")
  private String firstName;

  @Size(min = 3, max = 50, message = Constants.LAST_NAME_ERROR_MESSAGE)
  @Column(name = "last_name")
  @ApiModelProperty(notes = "The User last name")
  private String lastName;

  @Lob
  @Column(name = "user_photo")
  @Basic(fetch = FetchType.LAZY)
  @ApiModelProperty(notes = "The User photo")
  private byte[] userPhoto;

  @Lob
  @Column(name = "cover_photo")
  @Basic(fetch = FetchType.LAZY)
  @ApiModelProperty(notes = "The User cover photo")
  private byte[] coverPhoto;

  @Column(name = "userPhotoVisibility")
  @ApiModelProperty(notes = "The User visibility of profile")
  private Boolean userPhotoVisibility = true;

  @Column(name = "nameVisibility")
  @ApiModelProperty(notes = "The User visibility of profile")
  private Boolean nameVisibility = true;

  @Column(name = "enabled")
  @ApiModelProperty(notes = "The User status active/not active")
  private Boolean enabled = true;

  @LazyCollection(LazyCollectionOption.FALSE)
  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Post> userPosts;

  public User() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getNameVisibility() {
    return nameVisibility;
  }

  public void setNameVisibility(Boolean nameVisibility) {
    this.nameVisibility = nameVisibility;
  }

  public Boolean getUserPhotoVisibility() {
    return userPhotoVisibility;
  }

  public void setUserPhotoVisibility(Boolean userPhotoVisibility) {
    this.userPhotoVisibility = userPhotoVisibility;
  }

  public byte[] getUserPhoto() {
    return userPhoto;
  }

  public String getUserPhotoAsString() {
    if (userPhoto != null) {
      return "data:image/png;base64," + Base64.getEncoder().encodeToString(userPhoto);
    } else {
      return Constants.USER_DEFAULT_PHOTO;
    }
  }

  public void setUserPhoto(byte[] userPhoto) {
    this.userPhoto = userPhoto;
  }

  public byte[] getCoverPhoto() {
    return coverPhoto;
  }

  public String getCoverPhotoAsString() {
    if (coverPhoto != null) {
      return "data:image/png;base64," + Base64.getEncoder().encodeToString(coverPhoto);
    } else {
      return Constants.COVER_DEFAULT_PHOTO;
    }
  }

  public void setCoverPhoto(byte[] coverPhoto) {
    this.coverPhoto = coverPhoto;
  }

  public List<Post> getUserPosts() {
    return userPosts;
  }

  public void setUserPosts(List<Post> userPosts) {
    this.userPosts = userPosts;
  }
}