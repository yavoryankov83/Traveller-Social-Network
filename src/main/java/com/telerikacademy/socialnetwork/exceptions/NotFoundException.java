package com.telerikacademy.socialnetwork.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason =
        "Sorry, the User/Friend/Post/Comment or the other resources you are looking for are not found in application, because they do not exist!")
public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
