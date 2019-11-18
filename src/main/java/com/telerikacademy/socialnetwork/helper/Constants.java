package com.telerikacademy.socialnetwork.helper;

public final class Constants {

  private Constants() {
  }

  //USERS MESSAGES
  public static final String USER_DEFAULT_NAME =
          "Anonymous";
  public static final String USER_DEFAULT_PHOTO =
          "src/main/resources/static/default-images/default_user_photo.jpg";
  public static final String COVER_DEFAULT_PHOTO =
          "src/main/resources/static/default-images/default_cover_photo.jpg";
  public static final String EMAIL_VALIDATION_REGEX =
          "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:" +
                  "\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c" +
                  "\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
                  "\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|" +
                  "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                  "\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[" +
                  "\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09" +
                  "\\x0b\\x0c\\x0e-\\x7f])+)\\])";
  public static final String USERNAME_ERROR_MESSAGE =
          "Username should be more than 3 and less than 50 symbols.";
  public static final String PASSWORD_ERROR_MESSAGE =
          "Password should be more than 3 and less than 68 symbols.";
  public static final String EMAIL_ERROR_MESSAGE =
          "Email must be valid or unused.";
  public static final String FIRST_NAME_ERROR_MESSAGE =
          "Password should be more than 3 and less than 50 symbols.";
  public static final String LAST_NAME_ERROR_MESSAGE =
          "Password should be more than 3 and less than 50 symbols.";
  public static final String USER_NOT_FOUND_MESSAGE =
          "User not exists.";
  public static final String USER_ALREADY_EXISTS_MESSAGE =
          "User already exists.";
  public static final String USER_PASSWORD_NOT_MATCH_CONFIRMATION_PASSWORD_MESSAGE =
          "User password did't match confirmation password.";
  public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE =
          "User's email already exists.";
  public static final String USER_EMAIL_EMPTY_MESSAGE =
          "User email should not be empty.";
  public static final String USER_NOT_VALID_MESSAGE
          = "User should be valid.";
  public static final String USER_USERNAME_EMPTY_MESSAGE =
          "User username should not be empty.";
  public static final String USER_PASSWORD_EMPTY_MESSAGE =
          "User password should not be empty.";
  public static final String USER_PASSWORD_CONFIRMATION_EMPTY_MESSAGE =
          "User password confirmation should not be empty.";
  public static final String USER_NOT_LOGGED =
          "You are not logged and can' t see other users.";

  //POSTS MESSAGES
  public static final String CONTENT_ERROR_MESSAGE =
          "Content should be more than 3 and less than 255 symbols.";
  public static final String POST_NOT_FOUND_MESSAGE =
          "Post not exists.";
  public static final String LIKED_POST_NOT_FOUND_MESSAGE =
          "Liked post not exists.";
  public static final String LIKED_POST_ALREADY_EXISTS_MESSAGE =
          "Liked post already exists.";
  public static final String POST_ALREADY_EXISTS =
          "Post already created by user.";
  public static final String POST_CONTENT_EMPTY_MESSAGE =
          "Post content should not be empty.";
  public static final String POST_NOT_VALID_MESSAGE =
          "Post should be valid.";
  public static final String LOCATION_ERROR_MESSAGE =
          "Location should be less or equal to 255 symbols long.";
  public static final String POST_DEFAULT_PHOTO =
          "src/main/resources/static/default-images/default_post_photo.jpg";
  public  static final Integer POSTS_INITIAL_SIZE_PER_PAGE = 3;

  //COMMENTS MESSAGES
  public static final String LIKED_COMMENT_ALREADY_EXISTS_MESSAGE =
          "Liked comment already exists.";
  public static final String LIKED_COMMENT_NOT_FOUND_MESSAGE =
          "Liked comment not exists.";
  public static final String COMMENT_NOT_FOUND_MESSAGE =
          "Comment not exists.";
  public static final String COMMENT_NOT_VALID_MESSAGE =
          "Comment should be valid.";

  //COMMON MESSAGES
  public static final String EMPTY_STRING = "";
  public static final String CONTENT_EMPTY_MESSAGE =
          "Content should not be empty.";

  //FRIENDSHIPS MESSAGES
  public static final String NO_FRIENDSHIP_MESSAGE =
          "Users are not in friendship.";
  public static final String FRIENDSHIP_EXISTS_MESSAGE =
          "Friendship already exists.";
  public  static final Integer FRIENDSHIP_BLOCKED_STATUS = 4;
  public  static final Integer FRIENDSHIP_ACCEPTED_STATUS = 2;
}